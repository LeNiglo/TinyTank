# ServerTinyTank

Authoritative-ish game server: hosts one match, relays/validates KryoNet messages between
clients, and reports lifecycle/stats to the master server over REST. One JVM = one game host.

## Build & run

| Command | Effect |
| ------- | ------ |
| `mvn clean package` | Fat JAR (`jar-with-dependencies`) in `target/`, main class `com.lefrantguillaume.Main` |
| `java -jar target/ServerTinyTank-1.8-jar-with-dependencies.jar` | Run with **Swing/graphical** UI (default) |
| `java -jar … -c` (`--console`) | Run with **console** UI (commands; type `help`) |
| `docker build -t tinytank-server . && docker run -it -p 13333:13333 -p 13444:13444/udp tinytank-server` | Containerized (console mode) |

**Java 21** (modernized 2026; builds on JDK 26 via `maven.compiler.release`). The fat JAR's
manifest sets `Add-Opens` so KryoNet's old Kryo can reflect into the JDK at runtime. Working dir
must contain `tanks.json`, `obstacles.json`, `config.properties`, and `maps/` — read by relative
path at startup, not bundled in the JAR.

The container runs **console mode** (Swing can't run headless); the server only binds its ports
once a game is `start`ed from the console, so run it interactively (`-it` / compose `stdin_open`).

## Architecture

`Main` → `MasterController` (the orchestrator, an `Observable` **and** `Observer`) wires together
four collaborators and routes every event between them:

| Component | Package | Role |
| --------- | ------- | ---- |
| `GameController` (~700 lines) | `gameComponent/controllers/` | Core game logic; dispatches every client message via a big `instanceof` chain in `doTask` → `doMessageXxx` |
| `GameServer` | `networkComponent/gameServerComponent/` | KryoNet `Server`; receives messages, re-broadcasts (`sendToAllTCP` / `sendToTCP`) |
| `DataServer` | `networkComponent/dataServerComponent/` | REST client to the master via JDK `HttpClient` + Jackson (was Jersey 1.9): `init/update/stop_server`, `add/remove_user`, `add_game_stats` |
| `UserInterface` | `userInterface/` | `GraphicalUserInterface` (**Swing/AWT**, not JavaFX) or `ConsoleUserInterface`; start/stop game, pick map & mode |

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

- **`DataServer.initServer()` is short-circuited to `return true`** — the register + 30s/120s
  keep-alive flow stays commented out, so the server runs fully offline (the other REST calls —
  add/remove user, stats — do fire on game events but hit a dead master domain). Master URL + HTTP
  Basic credentials are hardcoded in `DataServer.postJson`.
- `tanks.json` is loaded from disk with a `// TODO get this from the data server` note.
- **Dependencies (modernized 2026):** Java 21; Jersey 1.9 → JDK `HttpClient` + Jackson; `jettison`
  is now a direct dep (was transitive via jersey-json) for config JSON parsing;
  `javafx.geometry.Rectangle2D` → `java.awt.geom.Rectangle2D`; `javafx.util.Pair` is **vendored**
  at `src/main/java/javafx/util/Pair.java` (org.openjfx's per-platform classifiers broke Docker
  builds). KryoNet 2.22.0-RC1 is kept (it is the latest; the project is abandoned).
- A pre-existing **init race**: the console input thread starts before `gameController` is
  assigned, so issuing `start` instantly (scripted input) can NPE. A human typing it a moment
  later is fine.
- Heavy `System.out`/`WindowController.addConsoleMsg` logging on hot paths (per move/position
  update) — noisy and not free.
- `maps/`, `tanks.json`, `obstacles.json` are a **shared contract** with the client and web bundle
  (see root `CLAUDE.md`); changing one here without the others desyncs gameplay.
- `MessageNeedMap` / `MessageDownload` are registered but marked "pas utilisé" — map sync happens
  via MD5 checksums in `MessageConnect`, not file transfer.
