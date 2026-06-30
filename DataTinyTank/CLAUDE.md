# DataTinyTank — Master Server

Node.js REST hub for TinyTank: accounts, JWT auth, the live game-server registry, and
match-stats aggregation. Game servers self-register here; the website and game clients call it.

## Run

```bash
node server.js          # executable bit is set; listens on PORT or 1337
```

| Env var                     | Default                                  | Used for                          |
| --------------------------- | ---------------------------------------- | --------------------------------- |
| `PORT`                      | `1337`                                   | HTTP listen port (`server.js`)    |
| `MONGO_URL`                 | `mongodb://localhost:27017/tiny-tank`    | Mongo connection (mongoskin)      |
| `WEB_URL`                   | `http://tinytank.dev`                    | Base for activation/download links |
| `AUTH_USER` / `AUTH_PASSWORD` | hardcoded fallbacks (see gotchas)      | HTTP Basic auth on **all** routes |
| `SMTP_HOST` / `SMTP_PORT` / `SMTP_USER` / `SMTP_PASSWORD` | — | express-mailer transport          |

Stack: Express 4, mongoskin 1.4 (Mongo driver 1.4), jwt-simple, bcrypt 0.8, express-mailer +
Jade, background-task, moment. All vintage — pinned, no lockfile discipline expected.

## Architecture

`server.js` connects Mongo, exposes four collection globals (`Servers`, `Users`, `Tanks`,
`Matches`) plus `ObjectID`, `bcrypt`, `moment`, `WEB_URL` as **globals** (no `var`), then loads
`config.js`, `plugins/router.js`, `plugins/background.js`. Handlers rely on those globals.

### Request pipeline (`config.js`, order matters)

morgan → `trust proxy` → CORS whitelist → **HTTP Basic auth (every route)** → body-parser.
CORS whitelist: `localhost`, `tinytank.dev`, `tinytank.com`, `lefrantguillaume.com`,
`tinytank.lefrantguillaume.com`.

### Routes (`plugins/router.js`)

`app.all('*', [token_auth])` runs the JWT middleware before every handler; a catch-all returns
JSON 404. Three surfaces, all under Basic auth:

| Prefix    | Source file     | Endpoints                                                                              |
| --------- | --------------- | -------------------------------------------------------------------------------------- |
| `/server` | `server_api.js` | `init_server`, `stop_server`, `update_last_active`, `change_map`, `add_user`, `remove_user`, `add_game_stats`, `get_tank_list` |
| `/client` | `client_api.js` | `login`, `list_servers` (+ `user_profile`, `get_tank_list` borrowed from web/server)   |
| `/web`    | `web_api.js`    | `register`, `login`, `active_account`, `list_servers`, `ladder`, `user_profile`, `get_infos`, `get_tank_list` |

All responses share the envelope `{ name, res, err }`. Most failures still return HTTP 200 with
`res:false`/`err` set, not a 4xx.

## Data model (schemaless MongoDB)

| Collection | Key fields |
| ---------- | ---------- |
| `users`    | `email` (lowercased), `username`, `password` (bcrypt, cost 8), `from`, `active` (bool, set true via `active_account`), `token` (JWT, persisted on login), `createdAt`/`updatedAt` |
| `servers`  | `name`, `ip` (`x-forwarded-for` ‖ remoteAddress), `ports.{udp,tcp}`, `users` (array of usernames), `map`, `started_at`, `last_active` |
| `matches`  | `name`, `created_at`, `users[]` each `{ id, kills, deaths, currentScore, nbShots, nbHits }` |
| `tanks`    | tank catalog; read-only via `get_tank_list` (no write path here — seed externally) |

## Key patterns

- **JWT flow** (`token_auth.js`): token read from `X-Access-Token` header, or `access_token` in
  body/query. Decoded with secret `jwtTokenSecret` (set in `server.js`), looks up the user, and
  attaches `req.user`. Issued at `/web/login` with `{ iss: userId, exp }`, 7-day expiry, stored
  on the user doc and reused if already present.
- **Server lifecycle**: game servers `POST /server/init_server` (returns the new `_id`), heartbeat
  via `update_last_active`, and `stop_server` on shutdown. `add_user`/`remove_user` push/pull the
  `users` array; `change_map` updates `map`.
- **Reaping** (`background.js`): every 30 s, `Servers.remove({ last_active < now-5min })`. A
  server that stops heartbeating disappears from the registry within ~5 min.
- **Stats aggregation** (`web_api.user_profile`): no aggregation pipeline — loads every match
  containing the user, sums kills/deaths/score/shots in JS, derives per-game averages (`*PG`).
  O(matches) per profile request.

## Gotchas

- **Hardcoded secrets**: `jwtTokenSecret` is a literal in `server.js`; `AUTH_USER`/`AUTH_PASSWORD`
  have long hardcoded fallbacks in `config.js`. Set the env vars in any real deployment.
- **`/web/ladder` is fully mocked** — returns a static 5-entry array (`TODO` to compute from DB).
- **`token_auth` failures fall through**: an expired token calls `res.end(...)` but does not
  `return`, and any decode error just calls `next()` — auth is effectively advisory; Basic auth is
  the real gate.
- **`client_api.login` cheat check is inverted/broken**: `!app.get('jwtTokenSecret') == req.body.secret`
  always evaluates `false == secret`, so the guard never triggers as intended.
- Several handlers assume the lookup found a doc (e.g. `user_profile` dereferences `exists` with no
  null check) — malformed input can throw.
- `add_game_stats` only responds on success (`!err & result`, bitwise `&`); on error the request
  hangs with no reply.
