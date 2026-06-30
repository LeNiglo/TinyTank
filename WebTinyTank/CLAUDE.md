# WebTinyTank

Public website/portal for TinyTank. A **Nuxt 3 (Vue 3 + TypeScript)** app that renders the
marketing pages, account flows (login/register/profile), ladder, server list, and game download.
It holds **no game logic** — its Nitro server layer is a thin REST proxy (BFF) over the
`DataTinyTank` master server web API.

> See the root `CLAUDE.md` for the 4-tier architecture and how this fits the whole project.
> Rebuilt in 2026 from the legacy Meteor 1.2 app — see `docs/WebTinyTank-rebuild.md` and the
> spec/plan under `docs/superpowers/`.

## Run & build

| Command            | What it does                                             |
| ------------------ | -------------------------------------------------------- |
| `npm run dev`      | Nuxt dev server on port 3000                             |
| `npm run build`    | Production build → `.output/` (Nitro node-server)        |
| `npm run preview`  | Run the built server (`node .output/server/index.mjs`)   |
| `npm run test:unit`| Vitest unit tests (`tests/unit/`)                        |
| `npm run test:e2e` | Playwright E2E (`tests/e2e/`), one spec per page         |

> Node 22 (`.nvmrc`). The nvm shell wrapper in this environment is broken; use `command npm …`
> with `~/.nvm/versions/node/v22.16.0/bin` on PATH if `npm` recurses.

### Env vars (server-only, via `runtimeConfig`)

| Var          | Default                          | Purpose                                  |
| ------------ | -------------------------------- | ---------------------------------------- |
| `API_URL`    | `http://tinytank.dev/api/web`    | Base URL of the master server web API    |
| `API_AUTH`   | hardcoded `user:pass` (dev only) | HTTP Basic auth for the proxy (server)   |
| `SENTRY_DSN` | _(unset → Sentry disabled)_      | Error reporting DSN (client + server)    |
| `PORT`       | `3000`                           | Nitro listen port                        |

## Stack

Nuxt 3 · Vue 3 · TypeScript · Nitro (BFF) · Tailwind (preflight **disabled**) + the original
**`bootstrap.cosmo.css`** dark-red theme served from `public/css/` (faithful port) · Vitest +
`@nuxt/test-utils` · Playwright · `@sentry/browser` + `@sentry/node` (env-gated).

## Directory layout

| Path                     | Role                                                                       |
| ------------------------ | -------------------------------------------------------------------------- |
| `app.vue`                | Root — `<NuxtLayout><NuxtPage/></NuxtLayout>`                              |
| `layouts/`               | `default` (container) and `full` (full-width banner: home, download)       |
| `pages/`                 | File-based routes (see table below)                                        |
| `components/`            | AppHeader, AppFooter, AlertStack, SearchBox, GlobalInfos, PaypalButton, LadderTable, ServerList, ProfileCard, TankCard, HomeBanner |
| `composables/`           | `useAuth`, `usePolling`, `useAlerts`, `useMasterApi`, `useTimeAgo`          |
| `server/api/web/`        | The 8 Nitro BFF routes (one per master endpoint)                           |
| `server/utils/master.ts` | `callMaster` + envelope unwrap + field whitelist + `toHttpError`           |
| `server/plugins/sentry.ts` | Nitro Sentry init (env-gated)                                            |
| `plugins/sentry.client.ts` | Client Sentry init (env-gated)                                          |
| `types/master.ts`        | Typed master-API response shapes                                           |
| `public/`                | css (Cosmo theme), img, fonts (glyphicons), and `downloads/TinyTank.zip`   |
| `tests/unit/`            | Vitest (`master.spec`, `useAuth.spec`, `usePolling.spec`)                  |
| `tests/e2e/`             | Playwright, one spec per page + `api-proxy.spec` + `helpers.ts`            |
| `tests/support/stub-master.mjs` | Deterministic master stub for E2E                                   |

## Key patterns

### BFF proxy layer (`server/api/web/*` + `server/utils/master.ts`)

Each route is a thin `defineEventHandler` that calls `callMaster(method, path, opts)` and wraps
errors with `toHttpError`. `callMaster` attaches HTTP Basic auth (`API_AUTH`, server-only) and
runs `unwrapEnvelope`, which turns the master's `{ name, res, err }` envelope into clean data or
a thrown `MasterError` (master signals failure as HTTP 200 with `res:false`/`err`). The browser
calls same-origin `/api/web/*` and never sees `API_AUTH`.

**Field whitelisting (security):** `/web/login` and `/web/user_profile` return the full Mongo user
doc *including the bcrypt password hash*. The BFF whitelists outgoing fields — `whitelistLogin`
→ `{ _id, username, token }`, `whitelistProfile` → `{ _id, username, from, createdAt, updatedAt,
stats }` — so the hash never reaches the browser. (The legacy Meteor app leaked it.)

The 8 routes: `infos`, `servers`, `ladder`, `profile`, `tanks` (GET); `register`, `login`,
`active` (POST). To add a feature, add a route here and a matching master endpoint.

### Auth (client-side, no cookie/session)

Faithful to the legacy app: a localStorage token echo, **no `accounts-*`, no session cookie**.
`useAuth()` exposes reactive `isConnected`/`username`/`authId` (via `useState`) backed by
localStorage keys `authToken`/`authID`/`authUsername`. Because localStorage is unavailable during
SSR, token-dependent UI (header logged-in state, profile-without-id, logout) **resolves on the
client** — call `auth.hydrate()` in `onMounted`. Login stores the session; logout clears it.

### Reactive polling

`usePolling(fn, ms)` runs `fn` on mount and every `ms`, clearing on unmount. Ladder & servers
poll 60s; global infos polls 120s. (`startPolling` is the pure, unit-tested core.)

### Alerts

`useAlerts().notify(message, title?, level?)` pushes a dismissible Bootstrap alert (rendered by
`AlertStack`), auto-removed after 5s. Levels: `warning` (default) / `success` / `danger`.

### Layouts & styling

`default` = container; `full` = full-width `banner.png` (home, download). The port reuses the
original `bootstrap.cosmo.css` + `style.css` (in `public/css/`) for a pixel-faithful look;
Tailwind is available but its preflight is disabled so it doesn't reset Bootstrap 3 markup.

## Routes (`pages/`)

| Path                  | File                       | Notes                                              |
| --------------------- | -------------------------- | -------------------------------------------------- |
| `/`                   | `index.vue`                | `full` layout; 3s banner message rotation          |
| `/profile/:id?`       | `profile/[[id]].vue`       | no id → `authID` from localStorage; else `/login`  |
| `/tank/:id?`          | `tank/[[id]].vue`          | catalog from `getTankList` (net-new)               |
| `/download`           | `download.vue`             | `full` layout; links `public/downloads/TinyTank.zip` |
| `/login` `/register`  | `login.vue` `register.vue` | manual auth; register mirrors legacy validation    |
| `/active/:id`         | `active/[id].vue`          | calls `active`, alerts, redirects                  |
| `/forgot-password` `/reset-password` | `forgot-password.vue` `reset-password.vue` | **"coming soon"** placeholders (no master endpoint) |
| `/servers-list`       | `servers-list.vue`         | polled 60s                                         |
| `/ladder`             | `ladder.vue`               | polled 60s; top-3 styling                          |
| `/about`              | `about.vue`                | team / donate / legal, anchor sections             |
| `*`                   | `[...slug].vue`            | 404 catch-all                                      |

## Testing

- **Playwright** E2E: one spec per page, mocking the Nitro `/api/web/*` routes at the browser
  boundary (`page.route`) for deterministic rendering. `api-proxy.spec.ts` exercises the *real*
  proxy against `tests/support/stub-master.mjs` (the only test of envelope+whitelist end-to-end).
  The dev server's `API_URL` points at the stub during E2E (see `playwright.config.ts`).
  Use `gotoHydrated(page, path)` before typing into inputs (waits for hydration).
- **Vitest**: `server/utils/master.ts` (unwrap + whitelist) and the composables.

## Gotchas

- **Hardcoded `API_AUTH` fallback** in `nuxt.config.ts` is for dev only — always set `API_AUTH`
  (and `API_URL`) in any real deployment.
- **No game logic / no DB**: this app only relays to the master server.
- **Shared game data**: `public/downloads/` (e.g. `tanks.json`, maps) is a bundled copy of the
  game data shared with client/server — keep in sync (see root `CLAUDE.md`).
- **Forgot/reset are placeholders**: the master has no forgot/reset endpoint (the legacy flow was
  Meteor `accounts-ui`). A real flow needs new master endpoints + is a separate project.
- **SSR + localStorage**: never read localStorage during SSR; gate on `import.meta.client` or do
  it in `onMounted` (see `useAuth`).
