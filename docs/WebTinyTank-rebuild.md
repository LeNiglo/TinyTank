# WebTinyTank — rebuild brief

> Brief for a dedicated session. Goal: replace the legacy Meteor 1.2 site with a
> modern, maintained web frontend — **with automated frontend testing baked in from
> the start** so rendering can be verified programmatically (no manual eyeballing).

## TL;DR

The current `WebTinyTank` is a **Meteor 1.2.0.1 (2015)** app. A pragmatic upgrade to
Meteor 2.16 was attempted and *got it rendering*, but the project owner chose to
**rebuild fresh** instead of carrying the legacy Blaze/Iron Router/Meteor stack forward.
The app is tiny and has **no game logic** — it is a thin REST proxy over the master
server (`DataTinyTank`) plus some marketing/account pages. That makes a clean rebuild
low-risk and fast.

The working tree has been **reverted to the original 1.2 state** (the upgrade WIP was
discarded). Start from what's committed.

## What the app actually is (scope to reproduce)

A public portal. **Holds no game state**; every dynamic action calls a server-side
Meteor method that proxies an HTTP call to the master server's `/api/web` endpoints.

### Server methods → master API (the entire backend surface — 8 calls)

All are one-line proxies (`server/methods.js`) to `process.env.API_URL` (default
`http://tinytank.dev/api/web`) behind HTTP Basic auth (`API_AUTH`):

| Meteor method | Master endpoint | Used by |
| ------------- | --------------- | ------- |
| `getUserProfile` | `GET /user_profile` | profile page |
| `getGlobalInfos` | `GET /get_infos` | header/infos (poll 120s) |
| `getServersList` | `GET /list_servers` | servers page (poll 60s) |
| `getLadder` | `GET /ladder` | ladder page (poll 60s) |
| `getTankList` | `GET /get_tank_list` | tank page |
| `myRegister` | `POST /register` | register form |
| `active_account` | `POST /active_account` | activation link |
| `myLogin` | `POST /login` | login form |

Responses are raw HTTP; the client does `JSON.parse(result.content).res`.

### Pages / routes (~13)

`/` (home, animated banner), `/profile/:_id?`, `/tank/:_id?`, `/download`, `/login`,
`/register`, `/active/:_id` (calls `active_account` then redirects), `/forgot-password`,
`/reset-password`, `/servers-list`, `/ladder`, `/about`, `*` (404).

### Auth (no accounts package)

Fully manual. On login, store `authToken` / `authID` / `authUsername` in `localStorage`;
logout clears it. UI reactivity hangs off a single dependency that login/logout poke.
There is **no `accounts-*` package** and no real session — just localStorage + a token
echoed from the master server.

### Other behavior

- Reactive polling: ladder 60s, servers 60s, global infos 120s, home banner rotation.
- Flash-alert system (`myAlert(message, title, level)` → bootstrap alert, auto-dismiss 5s).
- Two layouts: default container + a full-width banner layout (home/download).
- Ships the game client download at `public/downloads/TinyTank.zip`, plus a **bundled copy
  of the shared game data** (`tanks.json`, maps, etc.) — see "Shared contract" below.

## Recommended target stack

Pick a modern, maintained, well-testable stack. Suggested default:

- **Vite + React (or Svelte)** SPA, or **Next.js** if SSR/SEO is wanted for the portal.
- **TypeScript**, with a typed API client for the 8 master-server endpoints.
- **Tailwind / a component lib** to replace the Bootstrap 3 + custom CSS.
- **Frontend testing from day one** (explicit owner requirement):
  - **Playwright** for end-to-end / rendering assertions (headless, scriptable — this is
    the key ask: every page must be programmatically verifiable).
  - Component/unit tests (Vitest + Testing Library) for the API client and views.
- The master API uses **HTTP Basic auth** + returns a `{ name, res, err }` envelope (most
  failures are HTTP 200 with `res:false`). Keep the credential server-side (a thin BFF /
  serverless proxy), exactly as the Meteor methods did — do **not** expose `API_AUTH` to
  the browser.

> Meteor is **not** required. The app is a REST proxy + static pages; any modern SPA/SSR
> framework fits. Dropping Meteor also removes the dead MongoDB dependency (it was unused).

## Hard-won learnings from the 1.2 → 2.16 attempt (save the next dev the pain)

These are why the rebuild was chosen, and gotchas if anyone tries to upgrade again:

1. **Meteor `jquery@3` and `babel-runtime` are npm shims** — the meteor packages require
   the actual `jquery` and `@babel/runtime` npm packages in `node_modules`, or the client
   bundle dies with `jQuery not found` / `meteorInstall is not defined` and **nothing
   renders**. (`meteor npm install jquery @babel/runtime`.)
2. **`.meteor/packages` order matters.** A 1.2 file listing `iron:router` before
   `meteor-base` made the server bundle load `underscore.js` *before* the core `meteor`
   package, crashing boot with `ReferenceError: Package is not defined`. `meteor-base` must
   come first.
3. **Skipped structural upgraders.** Editing `.meteor/release` by hand skips the upgraders
   `meteor update` normally runs (e.g. `1.5-add-dynamic-import-package`,
   `1.4.1-add-shell-server-package`). The app needs `ecmascript`, `dynamic-import`,
   `shell-server` added explicitly.
4. **Iron Router is incompatible with modern Blaze 3.** `iron:router` pins `blaze@1.x`;
   modern `blaze-html-templates` wants `blaze@3`. A *non-greedy* resolve keeps Iron Router
   on Blaze 2.x and works on Meteor 2.16, but Blaze 3 forces a router migration anyway.
5. **Removed Blaze APIs:** `UI.registerHelper` → `Template.registerHelper`,
   `Deps.Dependency` → `Tracker.Dependency`, and `Template.x.created/rendered/destroyed =`
   → `Template.x.onCreated/onRendered/onDestroyed(...)`.
6. **Kadira is dead.** `meteorhacks:kadira` points at a hosted APM service shut down ~2019.
   There is currently **no error reporting at all**. The rebuild should add **Sentry**
   (`@sentry/browser` + `@sentry/node`). (Note: if you ever target Meteor 2.x, it runs on
   **Node 14**, so Sentry **v7** is required — v8+ needs Node 16+.)

## Shared contract — do not break

`tanks.json`, `obstacles.json`, and the map JSON files are a **single shared contract**
across the client, the game server, *and* the web download bundle. The website ships a
copy in `public/downloads/`. If gameplay data changes, all copies must stay in sync (see
root `CLAUDE.md`).

## First steps for the new session

1. Read the current `WebTinyTank/` (it's back to clean 1.2) to confirm pages/behavior.
2. Confirm the master server is reachable / `DataTinyTank` is the API (it was modernized —
   `node server.js`, see `DataTinyTank/CLAUDE.md`).
3. Decide framework (default: Vite + React + TS + Playwright).
4. Scaffold, port the 8-method API client behind a server-side proxy, rebuild the ~13
   pages, wire Sentry, and write Playwright tests page-by-page **as you build each page**.
5. Replace the Meteor build/deploy in root `build.sh` accordingly.
