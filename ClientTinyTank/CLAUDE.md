# ClientTinyTank

The playable desktop game client. Renders the arena with Slick2D/LWJGL, drives a Nifty
UI, talks to a game server over KryoNet (TCP/UDP) for gameplay, and to the master server
over REST/JSON for login and the server list. Java 1.8, Maven, `com.lefrantguillaume`.

## Build & run

```bash
mvn clean package                                   # -> target/ClientTinyTank-<ver>-jar-with-dependencies.jar
java -jar target/ClientTinyTank-2.3-jar-with-dependencies.jar
```

- Main class: `com.lefrantguillaume.Main` -> builds `MasterGame` and calls `start()`.
- Needs a display + native LWJGL libs (windowed mode). No headless / unit tests in this module.
- **Run from the module root**: config JSON files are read from the current working
  directory (plain `new File`, see `StringTools.readFile`), not the classpath.

## Architecture

`MasterGame` is the root coordinator. It owns three things wired by a shared task bus:
`Windows` (Slick `NiftyStateBasedGame`), `NetworkController` (KryoNet client), and a
`masterTask`. `MasterGame.update()` routes each task to either `Windows` or
`NetworkController` based on the task's target.

| Path                                            | Role                                                                 |
| ----------------------------------------------- | -------------------------------------------------------------------- |
| `master/MasterGame.java`                        | Root coordinator; observer of `masterTask`; routes WINDOWS vs NETWORK |
| `components/graphicsComponent/graphics/`        | `Windows` (state machine), `EnumWindow`, per-screen `WindowXxx`       |
| `components/graphicsComponent/userInterface/`   | On-screen elements, overlays (`Overlay`, chat, tables, sliders)      |
| `components/graphicsComponent/input/`           | `InputGame` / `InputData` keybindings, `EnumInput`                   |
| `components/graphicsComponent/sounds/`          | `SoundController`, `MusicController`, `EnumSound`                    |
| `components/gameComponent/controllers/`         | `GameController` (core loop/state), `MapController`, `AccountController`, `InterfaceController` |
| `components/gameComponent/gameObject/`          | `Tank`, `Shot`, `Obstacle`, `Spell` (+ factories) and `EnumGameObject` |
| `components/gameComponent/animations/`          | `Animator` + `AnimatorXxxFactory` (game / interface / overlay sprites) |
| `components/gameComponent/playerData/`          | `Player`, `User`, `PlayerAction`, action enums                       |
| `components/gameComponent/RoundData/`           | `RoundController`, `Team`, `EnumGameMode`, `EnumTeams`               |
| `components/networkComponent/networkGame/`      | KryoNet `NetworkController` (client) + `NetworkRegister` + messages   |
| `components/networkComponent/networkData/`      | Jersey REST to master (`DataServer`, `Authentification`, `ServerList`) |
| `components/collisionComponent/`                | `CollisionController`, `CollisionDetection`, `CollisionObject`        |
| `components/taskComponent/`                     | The event bus: `GenericSendTask`, `TaskFactory`, `EnumTargetTask`    |
| `utils/configs/`                                | `MasterConfig`, `WindowConfig`, `CurrentUser`, `NetworkServerConfig` |

## Key patterns

- **Task / event bus (Observer)**: `GenericSendTask extends Observable implements Observer`
  is the only inter-component channel. Build a task with
  `TaskFactory.createTask(sender, target, payload)` (a `Tuple<EnumTargetTask, EnumTargetTask, Object>`)
  and `sendTask(...)` it. `EnumTargetTask` is **hierarchical**: e.g. `GAME` and `INTERFACE`
  are children of `WINDOWS`, `GAME_OVERLAY` of `GAME`, `MESSAGE_SERVER`/`CONFIG_SERVER` of
  `NETWORK`. Routing uses `task.getV2().isIn(EnumTargetTask.WINDOWS|NETWORK)`, so new event
  types must declare their parent in the enum or they won't route. `GenericSendTask`s are
  chained (each forwards notifications upward) so nested controllers can reach `masterTask`.
- **Screen state machine**: `Windows` is a Nifty `NiftyStateBasedGame` whose states are
  `WindowLogin -> WindowAccount -> WindowInterface -> WindowGame` (ids in `EnumWindow`:
  LOGIN/ACCOUNT/INTERFACE/GAME, EXIT=-1). Screen switches are driven by tasks.
- **Config-driven game objects**: tanks, weapons, spells, boxes and obstacles are built by
  factories (`TankFactory`, `SpellFactory`, `ObstacleFactory`) from JSON, not hardcoded.
  Three tank types live in `tanks.json`: **tiger** (rocket + shield + wall), **sniper**
  (charged laser + invisibility), **rusher** (machine-gun + rush + mine). Object kinds are
  enumerated in `EnumGameObject` (note distinct `_ENEMY` variants for rendering opponents).
- **Collision**: each game object registers `CollisionObject`(s) (axis offset `Block`s from
  the JSON `collisions` arrays) into `CollisionController`. `checkCollision` resolves with a
  priority map and reports IN/OUT/NOTHING (`EnumCollision`); `CollisionDetection` does the
  rectangle/angle geometry. Collisions are also a network message (server-authoritative).
- **Networking — two stacks**:
  - *Gameplay* = KryoNet. Every wire type extends `MessageModel` (`pseudo`/`id`/`playerAction`)
    and **must be registered in `NetworkRegister.register()` in the exact same order as the
    server** — Kryo serialization is order-sensitive, so the client and server registration
    lists are a shared contract. Messages live in `networkGame/messages/msg/` and cover
    connect/disconnect, move/shoot/spell, player new/delete/revive/update-state/position,
    obstacle put/update, shot update, round start/end/kill/score, chat, change-team,
    collision. Inbound server messages become `MESSAGE_SERVER -> GAME` tasks.
  - *Account / lobby* = Jersey REST (`networkData/DataServer`) to the master server at
    `http://tinytank.lefrantguillaume.com/api/client/` (`login`, `list_servers`),
    behind HTTP Basic auth.

## Config files (module root, read from CWD)

| File                         | Purpose                                                         |
| ---------------------------- | --------------------------------------------------------------- |
| `tanks.json`                 | Stats + weapon/spell/box + collision boxes per tank type        |
| `obstacles.json`             | Obstacle definitions (walls, mines, ...)                        |
| `configDataOverlay.json`     | French in-game tooltip text for each tank's hit/spell/box       |
| `configPreferenceOverlay.json` | Overlay layout/preferences                                    |

`tanks.json` / `obstacles.json` are a cross-module contract — keep them in sync with
`ServerTinyTank` and the web download bundle (see root CLAUDE.md).

## Gotchas

- **Hardcoded local server**: `MasterConfig` points the KryoNet client at `127.0.0.1`
  TCP `13333` / UDP `13444`. Change there to hit a remote game server.
- **Hardcoded REST secrets**: master Basic-auth credentials and the shared secret string
  are inline in `DataServer.java`.
- **Missing `configInput.json`**: `WindowGame`/`WindowAccount` load `configInput.json` via
  `InputData`, but the file is not in the module — provide one (or it falls back/throws) when
  running the input screens.
- `tanks.json` (and friends) contain `//` comments — they are parsed by Jettison's lenient
  reader, not strict JSON. Keep that in mind before reformatting with a strict tool.
- The `assets/old/interface/*.xml` Nifty screens are legacy; the active UI is built in code
  under `userInterface/`.
- Vintage stack (Java 1.8, Slick2D, Nifty 1.4.2-SNAPSHOT, Jersey 1.9) pulled from the
  `nifty-gui.sourceforge.net` Maven repo over plain HTTP.
