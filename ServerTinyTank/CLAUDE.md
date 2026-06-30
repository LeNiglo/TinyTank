# ServerTinyTank

Authoritative-ish game server: hosts one match, relays/validates KryoNet messages between
clients, and reports lifecycle/stats to the master server over REST. One JVM = one game host.

## Build & run

| Command | Effect |
| ------- | ------ |
| `mvn clean package` | Fat JAR (`jar-with-dependencies`) in `target/`, main class `com.lefrantguillaume.Main` |
| `java -jar target/ServerTinyTank-1.8-jar-with-dependencies.jar` | Run with **JavaFX/graphical** UI (default) |
| `java -jar … -c` (`--console`) | Run with **console** UI (commands; type `help`) |

Java 1.8 only. Working dir must contain `tanks.json`, `obstacles.json`, `config.properties`,
and `maps/` — they are read by relative path at startup, not bundled in the JAR.

## Architecture

`Main` → `MasterController` (the orchestrator, an `Observable` **and** `Observer`) wires together
four collaborators and routes every event between them:

| Component | Package | Role |
| --------- | ------- | ---- |
| `GameController` (~700 lines) | `gameComponent/controllers/` | Core game logic; dispatches every client message via a big `instanceof` chain in `doTask` → `doMessageXxx` |
| `GameServer` | `networkComponent/gameServerComponent/` | KryoNet `Server`; receives messages, re-broadcasts (`sendToAllTCP` / `sendToTCP`) |
| `DataServer` | `networkComponent/dataServerComponent/` | Jersey REST client to the master (`init/update/stop_server`, `add/remove_user`, `add_game_stats`) |
| `UserInterface` | `userInterface/` | `GraphicalUserInterface` (JavaFX) or `ConsoleUserInterface`; start/stop game, pick map & mode |

Supporting `gameComponent/` packages: `gameMode/` (controller + `Team` + abstract `GameMode`),
`gameMode/modes/` (the four concrete modes), `gameobjects/` (`tanks/`, `shots/`, `obstacles/`,
`player/` — each with `*ConfigData` + `*Factory` loaded from JSON), `maps/` (`Map`, `MapController`),
`collisionVote/`, `target/Targets` (live registry of players/shots/obstacles in the running match).

## Key patterns

- **Observable + task routing.** Components never call each other directly. They `notifyObservers`
  with a `Pair<EnumTargetTask, Object>`; `MasterController.update` reads the `EnumTargetTask` key
  (`GAME` / `NETWORK` / `MASTER_SERVER` / `MASTER_CONTROLLER`) and forwards the value to the right
  collaborator's `doTask(Observable, Object)`. To add cross-component behavior, emit a Pair — do
  not add a direct reference.
- **Message-driven protocol.** All 24+ network DTOs extend `MessageModel` and live in
  `gameServerComponent/clientmsgs/`. **Every class sent over the wire must be registered in
  `NetworkRegister.register()`**, in an order that **matches the client's `NetworkRegister`
  exactly** — KryoNet keys serialization by registration order, so a mismatch corrupts decoding.
  `GameController` mostly relays messages back to all clients (often via `RequestFactory` →
  `Request`); it is a thin authority, trusting client-reported positions/moves.
- **Pluggable game modes.** Subclass `GameMode` (`FreeForAll`, `TeamDeathMatch`, `TouchDown`,
  `Kingdom`), implement `createObstacles` + `doTask(Pair<EnumAction,Object>, data)`, and add the
  value to `EnumGameMode`. `GameModeController` holds the active mode.
- **Distributed collision voting (anti-cheat).** Clients detect their own hits and send
  `MessageCollision`. `CollisionVoteController` groups votes for the same
  (hitterId, targetId, type) into a `CollisionVoteElement`. On the first vote a 200 ms timer
  (`GameController.addCollisionTimer`) starts; when it fires, a collision is applied only if
  `playerCount <= 2` **or** `numberVote > playerCount / 2` (majority). This trades latency for
  cheat resistance — no single client can fabricate a hit.

## Config files

| File | Holds |
| ---- | ----- |
| `config.properties` | `tcpPort=13333`, `udpPort=13444`, `maxAllowedPlayers=8`, `maxAllowedPing=100`, `gameName`; code also reads `friendlyFire` / `allyNoBlock` (loaded into static `ServerConfig`) |
| `tanks.json` | Tank stats/weapons/spells → `TankConfigData` |
| `obstacles.json` | Obstacle defs → `ObstacleConfigData` |
| `maps/*.json` | Map layouts; **a map loads only if a matching `maps/<name>.jpg` exists** (see `MasterController.loadMaps`) |

## Gotchas

- **`DataServer.initServer()` is short-circuited to `return true`** — the whole REST flow
  (register, 30s/120s keep-alive, stats) is commented out. The server currently runs fully offline;
  master-server URL and HTTP Basic credentials are hardcoded in `DataServer.getClientResponse`.
- `tanks.json` is loaded from disk with a `// TODO get this from the data server` note.
- Heavy `System.out`/`WindowController.addConsoleMsg` logging on hot paths (per move/position
  update) — noisy and not free.
- `maps/`, `tanks.json`, `obstacles.json` are a **shared contract** with the client and web bundle
  (see root `CLAUDE.md`); changing one here without the others desyncs gameplay.
- `MessageNeedMap` / `MessageDownload` are registered but marked "pas utilisé" — map sync happens
  via MD5 checksums in `MessageConnect`, not file transfer.
