# WebTinyTank

Public website/portal for TinyTank. A Meteor 1.2 app that renders the marketing pages, account
flows (login/register/profile), ladder, server list, and game download. It holds **no game
logic** — its server side is a thin REST proxy over the `DataTinyTank` master server web API.

> See the root `CLAUDE.md` for the 4-tier architecture and how this fits the whole project.

## Run & build

| Command          | What it does                                  |
| ---------------- | --------------------------------------------- |
| `meteor run`     | Dev server on port 3000 (auto-reload)         |
| `meteor build`   | Production bundle (deployed as `tinytankweb`) |

### Env vars (server)

| Var        | Default (set in `server/lib/startup.js` if unset) | Purpose                          |
| ---------- | ------------------------------------------------- | -------------------------------- |
| `API_URL`  | `http://tinytank.dev/api/web`                     | Base URL of master server web API |
| `API_AUTH` | hardcoded `user:pass` basic-auth string           | HTTP basic auth for the proxy     |

## Stack

Meteor 1.2.0.1 · Blaze/Spacebars (`blaze-html-templates`) · Iron Router · MongoDB (unused for
game data — state lives in Session/localStorage) · jQuery + Bootstrap (loaded as static assets
from `public/`, not via npm) · Kadira APM (`meteorhacks:kadira`) · FontAwesome · momentjs.

## Directory layout

| Path                  | Role                                                                 |
| --------------------- | ------------------------------------------------------------------- |
| `client/main.html`    | `<head>` only — pulls in `public/css` + `public/js` (Bootstrap)      |
| `client/main.js`      | Global helpers: `myAlert`, `isUserConnected`, `getUsername`, `activeIfTemplateIs` |
| `client/layouts/`     | `layout` (default) and `fullLayout` (banner home/download)          |
| `client/partials/`    | header, footer, login/register/forgot/reset, infos, error-item, loading, paypal |
| `client/views/`       | home, profile, ladder, servers, search, about, download             |
| `server/methods.js`   | All Meteor methods — each is a one-line REST proxy via `myShortHttp` |
| `server/lib/startup.js` | Sets `API_URL` / `API_AUTH` defaults on startup                   |
| `server/kadira.js`    | Kadira connect (hardcoded appId/secret)                             |
| `lib/router.js`       | Iron Router route map (runs on client + server)                     |
| `public/`             | css, js, img, fonts, and `downloads/TinyTank.zip` (game client + data) |

## Key patterns

### REST proxy layer (`server/methods.js`)

Every method is `this.unblock()` + `myShortHttp(method, path, datas)`, which calls
`Meteor.http.call(API_URL + path, { params, auth: API_AUTH })`. Errors are swallowed (logged,
returns `null`). The HTTP response is returned raw — clients parse `JSON.parse(result.content).res`.
To add a feature, add a method here and a matching endpoint on the master server. Existing methods:
`getUserProfile`, `getGlobalInfos`, `getServersList`, `getLadder`, `myRegister`,
`active_account`, `myLogin`, `getTankList`.

### Auth (client-side, no Meteor accounts)

Login (`partials/login/login.js`) calls `myLogin`, then stores `authToken`, `authID`,
`authUsername` in `localStorage`. There is **no `accounts-*` package** — auth is fully manual.
- `isUserConnected` helper reads localStorage and depends on the `isUserConnectedDeps`
  reactive dep; call `isUserConnectedDeps.changed()` after login/logout to re-render.
- Logout (`partials/header.js`) does `localStorage.clear()`.
- Router also mirrors a token into `Session.get('authToken')` for its `onBeforeAction` guard.

### Reactive polling

Views poll the proxy methods on `Meteor.setInterval` and push results into `Session`:
ladder 60s (`views/ladder`), server list 60s (`views/servers`), global infos 120s
(`partials/infos`), home banner message rotation (`views/home`). Clear intervals on route leave.

### Alerts

`myAlert(message, title, force)` (global, in `main.js`) renders `Template.errorItem` into
`#errors`, scrolls to top, and auto-removes after 5s. `force` is a Bootstrap class:
`warning` (default), `success`, `danger`.

### Layout switching

Default `layoutTemplate: 'layout'`. `home` and `download` override to `fullLayout` (full-width
banner). The header receives an `affix` param differing between the two layouts.

## Routes (`lib/router.js`)

| Path                  | Template       | Notes                                            |
| --------------------- | -------------- | ------------------------------------------------ |
| `/`                   | home           | `fullLayout`                                     |
| `/profile/:_id?`      | profile        | no id → uses `authID` from localStorage          |
| `/tank/:_id?`         | tank           |                                                  |
| `/download`           | download       | `fullLayout`                                     |
| `/login` `/register`  | login/register |                                                  |
| `/active/:_id`        | activation     | calls `active_account` then redirects to profile |
| `/forgot-password` `/reset-password` |  | password flows                                  |
| `/servers-list`       | servers-list   | polled                                            |
| `/ladder`             | ladder         | polled                                            |
| `/about`              | about          |                                                  |
| `*`                   | notFound       | 404 catch-all                                     |

## Gotchas

- **Hardcoded secrets**: `API_AUTH` default credentials in `server/lib/startup.js` and the
  Kadira appId/secret in `server/kadira.js` are committed in plaintext.
- **No game logic / no real DB use**: this app only relays to the master server; MongoDB is
  effectively unused — never add gameplay or persistence logic here.
- **Shared game data**: `public/downloads/TinyTank/` (e.g. `tanks.json`, map JSON) is a bundled
  copy of the game data shared with client/server — keep in sync (see root `CLAUDE.md`).
- **Vintage Meteor 1.2 / Blaze**: no React, no npm imports, no ES modules; client uses globals,
  jQuery, and `Session`. Static vendor JS/CSS lives in `public/`, not in a package manager.
- **Raw HTTP responses**: callers must `JSON.parse(result.content)` and read `.res` — there is
  no shared response wrapper or error normalization.
