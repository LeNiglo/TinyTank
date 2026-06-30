# WebTinyTank Rebuild — Design Spec

> Status: **approved design**, pre-implementation. Source brief: `docs/WebTinyTank-rebuild.md`.
> Supersedes the legacy Meteor 1.2 app in `WebTinyTank/`.

## 1. Goal

Replace the legacy Meteor 1.2.0.1 portal with a modern, maintained, **fully test-covered**
web frontend. The app holds **no game logic** — it is a thin REST proxy over the `DataTinyTank`
master server (8 endpoints) plus marketing/account pages. Frontend rendering must be
**programmatically verifiable** (Playwright), per the explicit owner requirement.

## 2. Decisions (locked)

| Topic | Decision |
| ----- | -------- |
| Framework | **Nuxt 3 (latest stable; 4.x if GA at scaffold time) + Vue 3 + TypeScript** |
| Backend-for-frontend | Nuxt **Nitro server routes** hold `API_AUTH`; browser never sees the credential |
| Styling | **Tailwind CSS**. **Faithful visual port** of the legacy dark-red "tank" identity — same pages, banner, structure; modern CSS underneath (no Bootstrap 3) |
| Forgot/reset password | **"Coming soon" placeholders.** No master endpoint exists (legacy relied on Meteor `accounts-ui`). A real rebuild is a separate, later project. |
| E2E testing | **Playwright**, one spec per page, written **as each page is built**. Tests mock the Nitro `/api/web/*` routes — deterministic, no live master needed. |
| Unit testing | **Vitest** for the envelope-unwrap util + composables (lighter coverage; optional but recommended). |
| Error reporting | **`@sentry/nuxt`**, DSN from env, **no-op when unset**. Replaces dead Kadira. |
| Deploy | Nitro `node-server` output run by the existing **`tinytankweb` systemd service**; rewrite `build.sh`'s `web()` from `meteor build` → `npm ci && npm run build`. Add a **Dockerfile**. |
| Secrets | `API_URL` / `API_AUTH` via Nuxt `runtimeConfig` (server-only) from env. Legacy hardcoded values kept **only** as dev fallback. |
| Shared contract | `public/downloads/` (game-client zip + bundled `tanks.json`/maps) carried forward **byte-for-byte**; never mutated here. |

## 3. Architecture

```
Browser (Vue 3 — SSR shell + client hydration)
   │  $fetch('/api/web/*')   — same-origin, NO credentials
   ▼
Nitro server routes  ──HTTP Basic (API_AUTH)──▶  DataTinyTank  /api/web/*
   server/api/web/*.ts                              (master server — UNTOUCHED)
   server/utils/master.ts  ← single place for: API_AUTH, the call, envelope unwrap, field whitelist
```

**Why Nuxt:** Nitro is the BFF the brief demanded (keeps `API_AUTH` server-side); SSR gives the
public portal real SEO; one framework, one deployable. Vite + Vue 3 + TS under the hood.

### Envelope normalization (the core backend job)

The master returns `{ name, res, err }` and signals **failure as HTTP 200 with `res:false`/`err`**.
The legacy client read this inconsistently (`results.data.res` for login vs `JSON.parse(content).res`
elsewhere). `server/utils/master.ts` normalizes this **once**:

- `res === false` (or `res === null` with `err`) → throw an `H3Error` with status 400/404 and the
  `err` message. The Vue side gets clean success data or a thrown error — never the raw envelope.
- On success, return `res` typed.

### Field whitelisting (security improvement over legacy)

`/web/login` and `/web/user_profile` return the **entire Mongo user doc — including the bcrypt
password hash**. The legacy app leaked this to the browser. The BFF **whitelists** outgoing fields:

- **login** → `{ _id, username, token }` only.
- **user_profile** → `{ _id, username, from, createdAt, stats }` only (no `password`, no `token`).

## 4. The 8 endpoints — typed contract

All under HTTP Basic auth on the master. Shapes derived from `DataTinyTank/apis/web_api.js`.

| Nitro route | Master | Method | Success `res` shape (post-whitelist) |
| ----------- | ------ | ------ | ------------------------------------ |
| `GET /api/web/infos` | `/get_infos` | GET | `{ last: { username, _id, createdAt } \| null, nb_users: number }` |
| `GET /api/web/servers` | `/list_servers` | GET | `Server[]` = `{ _id, name, ip, ports:{udp,tcp}, users:string[], map, started_at, last_active }` |
| `GET /api/web/ladder` | `/ladder` | GET | `LadderRow[]` = `{ rank, username, _id, gamesPlayed, killCount, accuracy }` (master-mocked, static 5) |
| `GET /api/web/profile?id=` | `/user_profile` | GET (`_idUser`) | `{ _id, username, from, createdAt, stats }`; `stats` = `{ gamesPlayed, kills, deaths, score, shotsFired, shotsHit, killsPG, deathsPG, scorePG, shotsFiredPG, shotsHitPG }` |
| `GET /api/web/tanks` | `/get_tank_list` | GET | `Tank[]` (tank catalog docs) |
| `POST /api/web/register` | `/register` | POST | `true` on success; else throws (`err`, e.g. "Email or Username already taken.") |
| `POST /api/web/login` | `/login` | POST (`login`,`password`) | `{ _id, username, token }`; else throws (`err`) |
| `POST /api/web/active` | `/active_account` | POST (`_idUser`) | mongo update result `{ ... }`; treat truthy as success |

**Validation parity:** register enforces the same regexes client-side as legacy
(`email`, `password` 7–99 chars, `username` `[\w\s]{3,20}`, password-confirm match) **and** the
master re-validates. Country options: `France, United Kingdom, Spain, Deutschland, Belgium,
United States, Other`.

## 5. Pages / routes (faithful to legacy ~13)

| Route | Render | Behavior |
| ----- | ------ | -------- |
| `/` (home) | SSR shell | full-banner layout; 3s rotating shuffle-letters message (`Have Fun` / `Get Blasted` / `Rank Up`, colors `#7F0000/#CC0000/#7F2626`) — client-only effect |
| `/profile/:id?` | SSR w/ id; **client-resolve** w/o id | no id → read `authID` from localStorage (client); none → redirect `/login`. Title = `<username>'s profile on TinyTank`. Accuracy = `shotsHit*100/shotsFired`. Dates via relative-time. |
| `/tank/:id?` | SSR | renders from `getTankList` (tank catalog) |
| `/download` | SSR | full-banner layout; links `public/downloads/TinyTank.zip` |
| `/login` | CSR form | calls login; on success store `authToken`/`authID`/`authUsername` in localStorage, redirect `/profile`, welcome alert; on `err` show danger alert |
| `/register` | CSR form | validates, calls register; on success redirect `/login` + "activation email sent" alert |
| `/active/:id` | client | calls `active`; success/error alert; redirect `/profile` |
| `/forgot-password` | static | **"Coming soon"** placeholder |
| `/reset-password` | static | **"Coming soon"** placeholder |
| `/servers-list` | SSR + poll 60s | live server registry table |
| `/ladder` | SSR + poll 60s | top-3 highlight styling (gold star #1; font-size & color ramp on ranks 1–3) |
| `/about` | SSR | static marketing/team page |
| `*` (404) | — | not-found page |

**SSR caveat (intentional):** auth is **localStorage token echo, no cookie** (faithful to legacy).
Anything token-dependent — header "logged in"/username, logout, profile-without-id — resolves
**client-side** after hydration. SSR renders the public shell; token-gated bits hydrate on the
client. No session cookie is invented.

## 6. Client building blocks (composables / components)

Replace the Meteor globals (`isUserConnectedDeps`, `UI.registerHelper`, per-template
`setInterval`) with composables:

- **`useAuth()`** — reactive `isConnected`, `username`, `authId`; `login(data)`, `logout()`
  (clears localStorage + redirects home). Replaces `isUserConnectedDeps` + `localStorage.clear()`.
- **`usePolling(fn, ms)`** — runs on mount, clears on unmount. Used by ladder (60s), servers
  (60s), infos (120s). Replaces manual `Meteor.setInterval`/`clearInterval`.
- **`useAlerts()`** — `notify(message, title, level)` → dismissible alert, auto-remove 5s.
  Levels: `warning` (default) / `success` / `danger`. Replaces global `myAlert`.
- **`useMasterApi()`** — typed wrappers over the 8 `$fetch('/api/web/*')` calls.

Components: `AppHeader` (nav + search + auth state), `AppFooter`, `GlobalInfos` (last user / user
count, polled 120s), `AlertStack`, `LadderTable`, `ServerList`, `ProfileCard`, `TankCard`,
`HomeBanner`. Two layouts: `default` (container) and `full` (full-width banner, used by home +
download).

Header search box → navigates to `/profile/<query>` (legacy tank-detection was a TODO; keep
profile-only).

## 7. Testing strategy

- **Playwright E2E**, one spec per page, authored alongside each page. Each spec **mocks the
  Nitro `/api/web/*` route(s)** the page uses (via `page.route`) so rendering is deterministic and
  no live master is required. Assertions: correct elements render, polled data updates, auth
  state toggles, alerts appear/dismiss, redirects fire.
- **Vitest unit**: `server/utils/master.ts` envelope unwrap (success → `res`; `res:false` →
  throws with `err`; field whitelist strips `password`/`token` where required) + `useAuth`/
  `usePolling` logic.
- Both wired into `package.json` scripts and runnable headless in CI.

## 8. Observability

`@sentry/nuxt` initialized on both client and server. DSN from `runtimeConfig.public.sentryDsn`
(client) / env (server). **When DSN is unset, Sentry init is skipped** — no errors, no dead
endpoints (unlike the abandoned Kadira).

## 9. Build & deploy

- **Dev:** `npm install && npm run dev` (Nuxt dev server). Point `API_URL` at a local/remote
  `DataTinyTank` (`http://localhost:1337/api/web`) or rely on mocked routes for UI work.
- **Prod build:** `npm ci && npm run build` → Nitro `node-server` output in `.output/`.
- **systemd:** `tinytankweb` runs `node .output/server/index.mjs` (replaces the Meteor bundle).
  Reads `API_URL`, `API_AUTH`, `SENTRY_DSN`, `PORT` from the service environment.
- **`build.sh`:** rewrite `web()` — replace `meteor update`/`meteor build` with the npm build,
  copy `.output/` under `/opt/tinytank/web/`, restart `tinytankweb`. Keep nginx in front.
- **Dockerfile:** multi-stage (build → slim Node runtime serving `.output/`), mirroring the
  `DataTinyTank`/`ServerTinyTank` Docker pattern.

## 10. Out of scope / non-goals

- No changes to `DataTinyTank` (the master) — keeps the parallel-work boundary clean.
- No real forgot/reset password flow (placeholder only).
- No game logic, no MongoDB, no persistence in the web app.
- No change to the shared game-data files in `public/downloads/`.
- No visual redesign — faithful port only (a redesign is a possible later project).

## 11. Migration / cutover

The new app lives in `WebTinyTank/`, replacing the Meteor tree (`.meteor/`, `client/`, `lib/`,
`server/`, `private/`). `public/` assets (css/img/fonts/downloads) are carried forward. Delete the
Meteor-specific files once parity + tests are green. The `WebTinyTank/CLAUDE.md` is rewritten to
describe the Nuxt app at the end.
