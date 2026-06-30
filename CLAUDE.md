# TinyTank

Real-time multiplayer 2D tank arena game with a 4-tier architecture. Players run a Java
desktop client that connects to Java game servers for low-latency gameplay; a Node.js master
server handles accounts/stats/server-registry, and a Meteor website is the public portal.

## Components

| Folder            | Role          | Stack                          | Talks to                       |
| ----------------- | ------------- | ------------------------------ | ------------------------------ |
| `ClientTinyTank`  | Game client   | Java 21, Slick2D/LWJGL 2, KryoNet, Nifty | Game server (TCP/UDP), Master (REST) |
| `ServerTinyTank`  | Game server   | Java 21, KryoNet               | Clients (TCP/UDP), Master (REST) |
| `DataTinyTank`    | Master server | Node 22, Express 5, mongodb 7  | All others (REST + JWT)        |
| `WebTinyTank`     | Website       | Meteor.js 1.2, Blaze, MongoDB  | Master (REST proxy)            |

Each folder has its own `CLAUDE.md` with technical details.

## Modernization status (2026)

Dependencies were swept module-by-module to the latest practical versions:

- **`DataTinyTank`** ✅ fully modernized (mongodb 7, Express 5, nodemailer 9; `npm audit` clean) + Dockerfile. Runs.
- **`ServerTinyTank`** ✅ modernized (Java 21, Jersey → `HttpClient`/Jackson, vendored `Pair`) + Dockerfile. Runs.
- **`ClientTinyTank`** ⚠️ dep graph fixed + builds on Java 21, but **cannot run on arm64** (Slick2D → LWJGL 2, no Apple Silicon natives). Port plan: [`docs/ClientTinyTank-libgdx-port.md`](docs/ClientTinyTank-libgdx-port.md).
- **`WebTinyTank`** ⛔ still Meteor 1.2 — slated for a **fresh rebuild**: [`docs/WebTinyTank-rebuild.md`](docs/WebTinyTank-rebuild.md).

See `docs/` for the two rebuild/port briefs (each is self-contained for a dedicated session).

## Architecture notes

- **Two networking styles by design**: KryoNet binary serialization for low-latency gameplay
  (client ↔ game server), REST/JSON for everything else (↔ master server).
- **Hub-and-spoke**: `DataTinyTank` is the central hub. Game servers self-register with it and
  are reaped if inactive >5 min; the website is a thin REST proxy over its web API.
- **Shared game data**: `tanks.json`, `obstacles.json`, and the map JSON files exist in the
  client, server, *and* the web download bundle. They must stay in sync across all three for
  gameplay to agree — treat them as a single shared contract.

## Build & deploy

- **Java modules** (`ClientTinyTank`, `ServerTinyTank`): `mvn clean package` → fat JAR (Java 21).
- **Docker**: `DataTinyTank` and `ServerTinyTank` have Dockerfiles (tested). `WebTinyTank` will get
  one after its rebuild; `ClientTinyTank` is a desktop GUI app, not containerized.
- **`build.sh`** (root): legacy deploy of `WebTinyTank` + `DataTinyTank` as systemd services
  (`tinytankweb`, `tinytankdata`) under `/opt/tinytank` behind nginx. The Java client/server are
  not deployed here — the client is distributed to players, game servers self-register.

## Caveats

- `WebTinyTank` is still vintage (Meteor 1.2 / Blaze) pending its rebuild; the other three modules
  were modernized in 2026 (see status above).
- Several hardcoded secrets/IPs (web `API_AUTH`, master basic-auth defaults, client → `127.0.0.1`).
  The master server now accepts `JWT_SECRET`/`AUTH_*` via env (`.env`), but keeps hardcoded fallbacks.
