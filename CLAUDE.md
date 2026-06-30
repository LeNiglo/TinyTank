# TinyTank

Real-time multiplayer 2D tank arena game with a 4-tier architecture. Players run a Java
desktop client that connects to Java game servers for low-latency gameplay; a Node.js master
server handles accounts/stats/server-registry, and a Meteor website is the public portal.

## Components

| Folder            | Role          | Stack                          | Talks to                       |
| ----------------- | ------------- | ------------------------------ | ------------------------------ |
| `ClientTinyTank`  | Game client   | Java 1.8, Slick2D, KryoNet, Nifty | Game server (TCP/UDP), Master (REST) |
| `ServerTinyTank`  | Game server   | Java 1.8, KryoNet              | Clients (TCP/UDP), Master (REST) |
| `DataTinyTank`    | Master server | Node.js, Express, MongoDB      | All others (REST + JWT)        |
| `WebTinyTank`     | Website       | Meteor.js 1.2, Blaze, MongoDB  | Master (REST proxy)            |

Each folder has its own `CLAUDE.md` with technical details.

## Architecture notes

- **Two networking styles by design**: KryoNet binary serialization for low-latency gameplay
  (client ↔ game server), REST/JSON for everything else (↔ master server).
- **Hub-and-spoke**: `DataTinyTank` is the central hub. Game servers self-register with it and
  are reaped if inactive >5 min; the website is a thin REST proxy over its web API.
- **Shared game data**: `tanks.json`, `obstacles.json`, and the map JSON files exist in the
  client, server, *and* the web download bundle. They must stay in sync across all three for
  gameplay to agree — treat them as a single shared contract.

## Build & deploy

- **Java modules** (`ClientTinyTank`, `ServerTinyTank`): `mvn clean package` → fat JAR.
- **`build.sh`** (root): deploys `WebTinyTank` and `DataTinyTank` as systemd services
  (`tinytankweb`, `tinytankdata`) under `/opt/tinytank` behind nginx. The Java client/server are
  not deployed here — the client is distributed to players, game servers self-register.

## Caveats

- Vintage stack throughout (Meteor 1.2, Java 1.8, Slick2D, mongoskin) — expect dated tooling.
- Several hardcoded secrets/IPs (web `API_AUTH`, master basic-auth defaults, client → `127.0.0.1`).
