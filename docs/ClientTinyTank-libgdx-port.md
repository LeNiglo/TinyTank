# ClientTinyTank — LWJGL 3 / libGDX port brief

> Brief for a dedicated session. Goal: make the game client run **natively on modern
> hardware (Apple Silicon / arm64) on a maintained stack**, by porting its presentation
> layer off the abandoned Slick2D + LWJGL 2 + Nifty stack.

## TL;DR

`ClientTinyTank` is a Java desktop game client. Its dependency graph was **modernized and
now builds** (Java 21, Nifty migrated to Maven Central, Jersey → `HttpClient`+Jackson — see
commit `chore(client): modernize deps...`). **But it cannot run on this machine** because:

- It renders via **Slick2D**, which sits on **LWJGL 2.9.3**.
- LWJGL 2 ships natives only for **x86_64** (linux/osx/windows) — **no arm64**. It was
  abandoned ~2014, before Apple Silicon existed.

So on an arm64 Mac it dies with `UnsatisfiedLinkError: no lwjgl in java.library.path`.

There is **no partial fix**: you cannot "just upgrade to LWJGL 3", because Slick2D (and the
Nifty LWJGL/Slick renderers) are LWJGL-2-only and dead. Running natively on arm64 requires
**replacing the whole render/UI/input/audio layer** — the recommended target is **libGDX**
(2D-first, built on LWJGL 3, actively maintained, has arm64 natives and a UI toolkit to
replace Nifty).

## Two options

| Option | Effort | Result |
| ------ | ------ | ------ |
| **A. Rosetta stopgap** (no rewrite) | ~minutes | Run the *current* build under an **x86_64 JDK via Rosetta 2** + staged x86 LWJGL2 natives + a display. Works today, but x86-only, not maintainable long-term. |
| **B. libGDX port** (this brief) | days–weeks | Native arm64, maintained, the real solution. A presentation-layer rewrite. |

Rosetta path (Option A), if you just want to see it run:
```bash
# needs an x86_64 JDK installed; natives staged into target/natives/ (lwjgl-platform:2.9.3:natives-osx)
arch -x86_64 /path/to/x86-jdk/bin/java \
  -Djava.library.path=target/natives \
  -jar target/ClientTinyTank-2.3-jar-with-dependencies.jar
```

## Why LWJGL 3 alone isn't enough (the coupling)

The client never calls LWJGL directly — it calls Slick2D + Nifty. Measured coupling:

- **53 of 139 Java files (~38%) import Slick2D** (`org.newdawn.slick.*`)
- **7 files import Nifty** (`de.lessvoid.*`)

Slick2D usage spans the entire front half:

| Slick2D area | Purpose | ~files |
| ------------ | ------- | ------ |
| `Graphics`, `Color`, `Image`, `Animation`, `SpriteSheet` | rendering + sprites | ~30 |
| `StateBasedGame`, `BasicGameState`, `GameContainer`, `AppGameContainer` | game loop + screen state machine | ~10 |
| `Input` | keyboard / mouse | 11 |
| `geom.Rectangle/Shape/Circle/Transform` | geometry **+ collision** | ~15 |
| `Sound`, `Music` | audio | 3 |
| `opengl.SlickCallable` | raw GL hooks | 2 |

Maintained status: **LWJGL 3 ✅**, **libGDX ✅** (active, large community), **Slick2D ❌**
(dead ~2014), **Nifty 1.4 ❌** (dead; Nifty 2 dormant).

## What to reuse vs rewrite

**Reuse (largely intact):**

- **Networking + wire protocol** — KryoNet client (`components/networkComponent/networkGame/`),
  all `MessageModel` subclasses, and `NetworkRegister`. ⚠️ **`NetworkRegister.register()` order
  must stay byte-for-byte identical to the server's** — Kryo keys serialization by registration
  order; a mismatch silently corrupts decoding. Do not reorder during the port.
- **REST-to-master** (`components/networkComponent/networkData/DataServer.java`) — already on
  `HttpClient` + Jackson; reusable as-is.
- **Config/data loading** and the JSON contract files (`tanks.json`, `obstacles.json`,
  overlay configs) — though they're parsed with Jettison's lenient reader (the files contain
  `//` comments — not strict JSON).
- The project's own `utils/stockage/Pair` / `Tuple` and most enum/data types.
- Game-state *logic* (rounds, teams, players, game modes) — but note collision logic uses
  Slick `geom` types, so the geometry/collision math needs remapping (libGDX `math` or
  `java.awt.geom`).

**Rewrite (Slick2D/Nifty-bound):**

- All rendering, sprite/animation drawing, the game loop and screen state machine
  (`Windows` is a Slick `NiftyStateBasedGame`: LOGIN → ACCOUNT → INTERFACE → GAME).
- Input handling, audio, and the **Nifty UI** (overlays, chat, tables, sliders, menus) →
  rebuild on **libGDX Scene2D** (or another maintained UI).

## Architecture notes carried over (from `ClientTinyTank/CLAUDE.md`)

- **Task/event bus** is the only inter-component channel: `GenericSendTask` (Observer) +
  `TaskFactory.createTask(sender, target, payload)`; routing keys off a **hierarchical**
  `EnumTargetTask` (`GAME`/`INTERFACE` under `WINDOWS`, etc.). This is engine-agnostic and
  worth preserving.
- **Config-driven game objects**: tanks/weapons/spells/obstacles built by factories from
  JSON, not hardcoded. Three tanks: tiger / sniper / rusher.
- **Hardcoded** game-server target in `MasterConfig` (`127.0.0.1:13333` TCP / `13444` UDP)
  and REST secrets in `DataServer.java`.
- **Missing `configInput.json`** in the repo — the input screens load it; provide one.

## Shared contract — do not break

`tanks.json` / `obstacles.json` / map JSON are a **single shared contract** with
`ServerTinyTank` and the web download bundle. Keep them in sync across all three (see root
`CLAUDE.md`).

## First steps for the new session

1. Decide: libGDX (recommended) vs raw LWJGL3+OpenGL.
2. Scaffold a libGDX project (gradle/maven), Java 21, with arm64 natives.
3. Lift the **network + protocol + data** layers over unchanged (guard `NetworkRegister`
   order against the server).
4. Rebuild the screen state machine (login → account → interface → game) on libGDX.
5. Reimplement rendering/animation, input, audio, and UI (Scene2D) screen by screen.
6. Remap collision/geometry off Slick `geom`.
7. Verify it launches and connects to a running `ServerTinyTank` (which is modernized and
   binds 13333/13444 — see `ServerTinyTank/CLAUDE.md`).
