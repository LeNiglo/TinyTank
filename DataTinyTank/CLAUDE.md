# DataTinyTank — Master Server

Node.js REST hub for TinyTank: accounts, JWT auth, the live game-server registry, and
match-stats aggregation. Game servers self-register here; the website and game clients call it.

## Run

```bash
nvm use                 # .nvmrc -> Node 22
npm install
npm start               # = node server.js; listens on PORT or 1337
# or: docker build -t tinytank-data . && docker run -p 1337:1337 --env-file .env tinytank-data
```

Config is loaded from a `.env` file via `dotenv` (see `.env.example`; `.env` is gitignored).

| Env var                     | Default                                  | Used for                          |
| --------------------------- | ---------------------------------------- | --------------------------------- |
| `PORT`                      | `1337`                                   | HTTP listen port (`server.js`)    |
| `MONGO_URL`                 | `mongodb://localhost:27017/tiny-tank`    | Mongo connection (mongodb driver) |
| `WEB_URL`                   | `http://tinytank.dev`                    | Base for activation/download links |
| `JWT_SECRET`                | hardcoded fallback (see gotchas)         | Secret for signing JWT tokens     |
| `AUTH_USER` / `AUTH_PASSWORD` | hardcoded fallbacks (see gotchas)      | HTTP Basic auth on **all** routes |
| `SMTP_HOST` / `SMTP_PORT` / `SMTP_USER` / `SMTP_PASSWORD` | — | nodemailer transport (optional in dev) |

Stack (modernized 2026): **Express 5**, the official **`mongodb` driver 7** (async/await; no more
mongoskin), `jwt-simple`, **bcrypt 6**, **nodemailer 9 + Pug** email templates (replaced
express-mailer/Jade), `dotenv`, `moment`, `morgan`, `cors`, `basic-auth-connect`. `package-lock.json`
committed; `npm audit` clean. Multi-stage `Dockerfile` + `.dockerignore` present.

## Architecture

`server.js` `await`s a `MongoClient` connection inside an async bootstrap, then exposes four
collection globals (`Servers`, `Users`, `Tanks`, `Matches`) plus `ObjectId`/`ObjectID`, `bcrypt`,
`moment`, `WEB_URL` as **globals**, sets `app.set('mailer', …)`, and loads `config.js`,
`plugins/router.js`, `plugins/background.js` before `app.listen`. Handlers rely on those globals.

### Request pipeline (`config.js`, order matters)

morgan → `trust proxy` → CORS whitelist (`cors` pkg) → **HTTP Basic auth (every route)** →
`express.json()` / `express.urlencoded()` (built into Express 5; body-parser removed). CORS
whitelist: `localhost`, `tinytank.dev`, `tinytank.com`, `lefrantguillaume.com`,
`tinytank.lefrantguillaume.com`.

### Routes (`plugins/router.js`)

`app.use(token_auth)` (pathless — Express 5 dropped bare `'*'`) runs the JWT middleware before
every handler; a pathless catch-all returns JSON 404. Three surfaces, all under Basic auth:

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

- **JWT flow** (`token_auth.js`): exported as a factory `require('./token_auth.js')(app)` so the
  middleware can reach `jwtTokenSecret`. Token read from `X-Access-Token` header or `access_token`
  in body/query; decoded, `await`s the user lookup, attaches `req.user`. Best-effort: missing/
  invalid/expired tokens fall through as anonymous (Basic auth is the real gate). Issued at
  `/web/login` with `{ iss: userId, exp }`, 7-day expiry, stored on the user doc and reused.
- **Server lifecycle**: game servers `POST /server/init_server` (returns the new `_id`), heartbeat
  via `update_last_active`, and `stop_server` on shutdown. `add_user`/`remove_user` push/pull the
  `users` array; `change_map` updates `map`.
- **Reaping** (`background.js`): a plain `setInterval` (the `background-task`/Redis dependency was
  removed) runs every 30 s and `await Servers.deleteMany({ last_active < now-5min })`. A server
  that stops heartbeating disappears from the registry within ~5 min.
- **Stats aggregation** (`web_api.user_profile`): no aggregation pipeline — loads every match
  containing the user, sums kills/deaths/score/shots in JS, derives per-game averages (`*PG`).
  O(matches) per profile request.

## Gotchas

- **Hardcoded secret fallbacks**: `JWT_SECRET`, `AUTH_USER`, `AUTH_PASSWORD` have hardcoded
  fallbacks in `server.js`/`config.js` — always set the env vars in any real deployment.
- **`/web/ladder` is still fully mocked** — returns a static 5-entry array (`TODO` to compute
  from DB). Untouched by the modernization.
- **`token_auth` is best-effort by design**: a missing/invalid/expired token never blocks the
  request; HTTP Basic auth is the real gate.

> Fixed during the 2026 modernization (were bugs): `token_auth` no longer references an undefined
> `app` (JWT now actually works), `add_game_stats` no longer hangs on error, `user_profile` guards
> a missing user, and the broken `client_api.login` "cheater" check (a no-op that threw on missing
> input) was removed.
