# WebTinyTank Rebuild Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the legacy Meteor 1.2 `WebTinyTank` portal with a Nuxt 3 + Vue 3 + TypeScript app whose Nitro server layer proxies the 8 master-server endpoints, faithfully reproducing every legacy page, with Playwright E2E coverage written per page.

**Architecture:** A Nuxt SSR app. The browser calls same-origin `/api/web/*` Nitro routes (no credentials); those routes attach HTTP Basic auth (`API_AUTH`) and proxy to `DataTinyTank`'s `/api/web/*`, normalizing the `{ name, res, err }` envelope (failure = HTTP 200 with `res:false`) into clean data-or-throw, and whitelisting user fields so the password hash never reaches the browser. Auth stays localStorage-token based (no cookie); token-dependent UI hydrates client-side.

**Tech Stack:** Nuxt 3 (latest stable; 4.x if GA), Vue 3, TypeScript, Tailwind CSS, Nitro (BFF), `@nuxt/test-utils` + Vitest (unit), `@playwright/test` (E2E), `@sentry/nuxt` (env-gated), Docker. Node 22.

## Global Constraints

- **Node 22** (matches `DataTinyTank`; `.nvmrc` → `22`).
- **No changes to `DataTinyTank`** (the master server) — preserves the parallel-work boundary with the ClientTinyTank instance.
- **`public/downloads/` is carried forward byte-for-byte** — the shared game-data contract (`tanks.json`, maps, `TinyTank.zip`); never mutated here.
- **`API_AUTH` must never reach the browser** — server-only `runtimeConfig`. Legacy hardcoded values are dev fallbacks only.
- **Faithful visual port** — same pages, identity (dark-red tank look), banner, layout, copy. Tailwind, no Bootstrap 3. No redesign.
- **Master envelope:** `{ name, res, err }`; failures are usually HTTP 200 with `res:false`/`err`. Normalize once in `server/utils/master.ts`.
- **Field whitelist:** login → `{ _id, username, token }`; profile → `{ _id, username, from, createdAt, updatedAt, stats }`. Never forward `password`.
- **Forgot/reset password = "coming soon" placeholders** (no master endpoint exists).
- **All work on branch `worktree-feat-web-rebuild`** in the worktree; never commit to `dev`/`master`.
- **Legacy source of truth:** the Meteor templates currently in `WebTinyTank/client/` are the conversion reference for markup/bindings; delete them only in the final cleanup task (Task 22).
- **Conventional commits**, type `feat`/`chore`/`docs`/`test`; scope `web`. Co-author trailer per repo convention.

## File Structure

New tree under `WebTinyTank/` (replaces `.meteor/`, `client/`, `lib/`, `server/`, `private/`):

```
WebTinyTank/
  package.json  nuxt.config.ts  tsconfig.json  tailwind.config.ts  .nvmrc  .gitignore
  app.vue
  assets/css/main.css           # Tailwind entry + ported bits of legacy style.css
  layouts/default.vue           # container layout (legacy `layout`)
  layouts/full.vue              # full-width banner layout (legacy `fullLayout`)
  pages/
    index.vue                   # /            (home, full layout)
    profile/[[id]].vue          # /profile/:id?
    tank/[[id]].vue             # /tank/:id?
    download.vue                # /download    (full layout)
    login.vue  register.vue
    active/[id].vue             # /active/:id
    forgot-password.vue  reset-password.vue   # "coming soon"
    servers-list.vue  ladder.vue  about.vue
    [...slug].vue               # 404
  components/
    AppHeader.vue  AppFooter.vue  AlertStack.vue  SearchBox.vue
    GlobalInfos.vue  PaypalButton.vue
    LadderTable.vue  ServerList.vue  ProfileCard.vue  TankCard.vue  HomeBanner.vue
  composables/
    useAuth.ts  usePolling.ts  useAlerts.ts  useMasterApi.ts
  server/
    utils/master.ts             # API_AUTH + call + envelope unwrap + whitelist
    api/web/
      infos.get.ts  servers.get.ts  ladder.get.ts  profile.get.ts  tanks.get.ts
      register.post.ts  login.post.ts  active.post.ts
  types/master.ts
  public/                       # css(legacy)/img/fonts/downloads — carried forward
  tests/
    unit/*.spec.ts              # Vitest
    e2e/*.spec.ts               # Playwright (one per page)
  Dockerfile  .dockerignore
  playwright.config.ts  vitest.config.ts
  CLAUDE.md                     # rewritten in Task 22
```

---

## Task 1: Scaffold Nuxt app, Tailwind, test runners, carry assets

**Files:**
- Create: `WebTinyTank/package.json`, `nuxt.config.ts`, `tsconfig.json`, `tailwind.config.ts`, `.nvmrc`, `app.vue`, `assets/css/main.css`, `playwright.config.ts`, `vitest.config.ts`
- Create: `WebTinyTank/tests/e2e/smoke.spec.ts`
- Preserve: `WebTinyTank/public/**` (move existing `public/` aside, scaffold, restore)
- Modify: `WebTinyTank/.gitignore`

**Interfaces:**
- Produces: a booting Nuxt dev server; `npm run dev`, `npm run build`, `npm run test:unit`, `npm run test:e2e` scripts; Tailwind active; `runtimeConfig` with `apiUrl`/`apiAuth` (server) + `sentryDsn` (public).

- [ ] **Step 1: Preserve public assets, then scaffold**

The legacy `public/` (img, fonts, css, downloads bundle) must survive scaffolding.

```bash
cd WebTinyTank
mv public ../_webtt_public_backup        # park the asset bundle (incl. downloads/)
# remove Meteor-only top-level dirs we are replacing (keep CLAUDE.md, .gitignore, .iml for now)
rm -rf .meteor client lib server private WebTinyTank.iml
# scaffold Nuxt into the current directory (answer: package manager npm, no git init)
npx nuxi@latest init . --packageManager npm --no-install --force
mv ../_webtt_public_backup public         # restore assets
```

- [ ] **Step 2: Write `package.json`** (merge scaffold output with these scripts/deps)

```json
{
  "name": "webtinytank",
  "private": true,
  "type": "module",
  "engines": { "node": ">=22" },
  "scripts": {
    "dev": "nuxt dev",
    "build": "nuxt build",
    "preview": "node .output/server/index.mjs",
    "postinstall": "nuxt prepare",
    "test:unit": "vitest run",
    "test:e2e": "playwright test"
  },
  "dependencies": {
    "nuxt": "latest",
    "vue": "latest"
  },
  "devDependencies": {
    "@nuxtjs/tailwindcss": "latest",
    "@nuxt/test-utils": "latest",
    "@playwright/test": "latest",
    "@sentry/nuxt": "latest",
    "@vue/test-utils": "latest",
    "happy-dom": "latest",
    "tailwindcss": "latest",
    "typescript": "latest",
    "vitest": "latest"
  }
}
```

- [ ] **Step 3: Write `nuxt.config.ts`**

```ts
export default defineNuxtConfig({
  compatibilityDate: '2025-01-01',
  modules: ['@nuxtjs/tailwindcss'],
  css: ['~/assets/css/main.css'],
  runtimeConfig: {
    // server-only — NEVER exposed to the client
    apiUrl: process.env.API_URL || 'http://tinytank.dev/api/web',
    apiAuth: process.env.API_AUTH || 'T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv:lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo',
    public: {
      sentryDsn: process.env.SENTRY_DSN || ''
    }
  },
  app: {
    head: {
      title: 'TinyTank',
      link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }]
    }
  }
})
```

- [ ] **Step 4: Write `assets/css/main.css`** (Tailwind entry; legacy accent colors as CSS vars)

```css
@tailwind base;
@tailwind components;
@tailwind utilities;

:root {
  --tt-red-1: #7F0000;
  --tt-red-2: #CC0000;
  --tt-red-3: #7F2626;
  --tt-ladder-1: #F15841;
  --tt-ladder-2: #A83A2A;
  --tt-ladder-3: #4D241E;
}
```

`tailwind.config.ts`:

```ts
import type { Config } from 'tailwindcss'
export default <Partial<Config>>{
  content: [
    './components/**/*.{vue,ts}',
    './layouts/**/*.vue',
    './pages/**/*.vue',
    './app.vue'
  ]
}
```

- [ ] **Step 5: Write `app.vue`**

```vue
<template>
  <div>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>
```

- [ ] **Step 6: Write `vitest.config.ts` and `playwright.config.ts`**

`vitest.config.ts`:

```ts
import { defineVitestConfig } from '@nuxt/test-utils/config'
export default defineVitestConfig({
  test: { environment: 'happy-dom', include: ['tests/unit/**/*.spec.ts'] }
})
```

`playwright.config.ts`:

```ts
import { defineConfig } from '@playwright/test'
export default defineConfig({
  testDir: './tests/e2e',
  use: { baseURL: 'http://localhost:3000' },
  webServer: {
    command: 'npm run dev',
    url: 'http://localhost:3000',
    reuseExistingServer: !process.env.CI,
    timeout: 120_000
  }
})
```

- [ ] **Step 7: Write the smoke E2E `tests/e2e/smoke.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('app boots and renders a page', async ({ page }) => {
  await page.goto('/')
  await expect(page).toHaveTitle(/TinyTank/)
})
```

- [ ] **Step 8: Append Nuxt/Playwright ignores to `.gitignore`**

```
.nuxt
.output
node_modules
.data
test-results
playwright-report
```

- [ ] **Step 9: Install deps and run the smoke test**

Run:
```bash
npm install
npx playwright install chromium
npm run test:e2e
```
Expected: `smoke.spec.ts` PASS (1 passed). The dev server boots and the home title is `TinyTank` (Nuxt default page renders under the title set in config).

- [ ] **Step 10: Commit**

```bash
git add WebTinyTank
git commit -m "feat(web): scaffold Nuxt 3 + Tailwind + Vitest/Playwright; carry public assets"
```

---

## Task 2: Master proxy utility — envelope unwrap + field whitelist (pure logic, TDD)

**Files:**
- Create: `WebTinyTank/server/utils/master.ts`
- Test: `WebTinyTank/tests/unit/master.spec.ts`

**Interfaces:**
- Produces:
  - `unwrapEnvelope<T>(payload: { name: string; res: T | false | null; err: string | null }): T` — returns `res` on success; throws `MasterError` (message = `err`) when `res === false`, or when `res == null && err`.
  - `whitelistLogin(user): { _id, username, token }`
  - `whitelistProfile(user): { _id, username, from, createdAt, updatedAt, stats }`
  - `callMaster<T>(method, path, opts): Promise<T>` — calls `API_URL+path` with Basic auth, then `unwrapEnvelope`. (Tested at integration level in Task 3.)
  - `class MasterError extends Error { statusCode: number }`

- [ ] **Step 1: Write the failing test `tests/unit/master.spec.ts`**

```ts
import { describe, it, expect } from 'vitest'
import { unwrapEnvelope, whitelistLogin, whitelistProfile, MasterError } from '../../server/utils/master'

describe('unwrapEnvelope', () => {
  it('returns res on success', () => {
    expect(unwrapEnvelope({ name: 'x', res: { a: 1 }, err: null })).toEqual({ a: 1 })
  })
  it('throws MasterError with err when res is false', () => {
    expect(() => unwrapEnvelope({ name: 'login', res: false, err: 'Account not active.' }))
      .toThrowError('Account not active.')
  })
  it('throws when res is null and err present', () => {
    expect(() => unwrapEnvelope({ name: 'user_profile', res: null, err: 'User not found.' }))
      .toThrowError('User not found.')
  })
  it('allows falsy-but-valid res like 0 or empty array', () => {
    expect(unwrapEnvelope({ name: 'x', res: [], err: null })).toEqual([])
  })
})

describe('field whitelists', () => {
  const user = { _id: '1', username: 'Bob', password: 'HASH', token: 'TOK', from: 'France', createdAt: 'd', updatedAt: 'u', stats: { kills: 3 } }
  it('login exposes only _id, username, token', () => {
    expect(whitelistLogin(user)).toEqual({ _id: '1', username: 'Bob', token: 'TOK' })
  })
  it('profile excludes password and token', () => {
    const p = whitelistProfile(user)
    expect(p).toEqual({ _id: '1', username: 'Bob', from: 'France', createdAt: 'd', updatedAt: 'u', stats: { kills: 3 } })
    expect((p as any).password).toBeUndefined()
    expect((p as any).token).toBeUndefined()
  })
})
```

- [ ] **Step 2: Run it to confirm it fails**

Run: `npm run test:unit`
Expected: FAIL — `unwrapEnvelope`/whitelist not exported.

- [ ] **Step 3: Implement `server/utils/master.ts`**

```ts
import { Buffer } from 'node:buffer'

export class MasterError extends Error {
  statusCode: number
  constructor(message: string, statusCode = 400) {
    super(message || 'Master server error')
    this.name = 'MasterError'
    this.statusCode = statusCode
  }
}

export interface Envelope<T> { name: string; res: T | false | null; err: string | null }

export function unwrapEnvelope<T>(payload: Envelope<T>): T {
  if (payload.res === false || (payload.res == null && payload.err)) {
    throw new MasterError(payload.err || 'Request failed')
  }
  return payload.res as T
}

export function whitelistLogin(u: any) {
  return { _id: u._id, username: u.username, token: u.token }
}

export function whitelistProfile(u: any) {
  return { _id: u._id, username: u.username, from: u.from, createdAt: u.createdAt, updatedAt: u.updatedAt, stats: u.stats }
}

export async function callMaster<T>(
  method: 'GET' | 'POST',
  path: string,
  opts: { query?: Record<string, any>; body?: Record<string, any> } = {}
): Promise<T> {
  const cfg = useRuntimeConfig()
  const auth = 'Basic ' + Buffer.from(cfg.apiAuth).toString('base64')
  const payload = await $fetch<Envelope<T>>(cfg.apiUrl + path, {
    method,
    headers: { Authorization: auth },
    query: opts.query,
    body: opts.body
  })
  return unwrapEnvelope<T>(payload)
}
```

- [ ] **Step 4: Run tests to confirm pass**

Run: `npm run test:unit`
Expected: PASS (all `master.spec.ts` cases green).

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/server/utils/master.ts WebTinyTank/tests/unit/master.spec.ts
git commit -m "feat(web): add master proxy util — envelope unwrap + field whitelist"
```

---

## Task 3: The 8 Nitro BFF routes

**Files:**
- Create: `server/api/web/infos.get.ts`, `servers.get.ts`, `ladder.get.ts`, `profile.get.ts`, `tanks.get.ts`, `register.post.ts`, `login.post.ts`, `active.post.ts`
- Create: `types/master.ts`
- Test: `tests/unit/api-login.spec.ts` (representative integration test)

**Interfaces:**
- Consumes: `callMaster`, `whitelistLogin`, `whitelistProfile`, `MasterError` from Task 2.
- Produces (browser-facing JSON, all same-origin under `/api/web/`):
  - `GET /api/web/infos` → `Infos`
  - `GET /api/web/servers` → `Server[]`
  - `GET /api/web/ladder` → `LadderRow[]`
  - `GET /api/web/profile?id=<idOrUsername>` → `Profile`
  - `GET /api/web/tanks` → `Tank[]`
  - `POST /api/web/register` body `{ username, email, password, from }` → `{ ok: true }`
  - `POST /api/web/login` body `{ login, password }` → `{ _id, username, token }`
  - `POST /api/web/active` body `{ id }` → `{ ok: true }`
  - Failures: thrown `MasterError` → HTTP 400 with `{ statusCode, message }`.

- [ ] **Step 1: Write `types/master.ts`**

```ts
export interface Infos { last: { username: string; _id: string; createdAt: string } | null; nb_users: number }
export interface Server { _id: string; name: string; ip: string; ports: { udp: number; tcp: number }; users: string[]; map: string; started_at: string; last_active: string }
export interface LadderRow { rank: number; username: string; _id: string; gamesPlayed: number; killCount: number; accuracy: number }
export interface ProfileStats { gamesPlayed: number; kills: number; deaths: number; score: number; shotsFired: number; shotsHit: number; killsPG: number; deathsPG: number; scorePG: number; shotsFiredPG: number; shotsHitPG: number }
export interface Profile { _id: string; username: string; from: string; createdAt: string; updatedAt: string; stats: ProfileStats }
export interface Tank { _id: string; [k: string]: any }
export interface LoginResult { _id: string; username: string; token: string }
```

- [ ] **Step 2: Write the GET routes**

`server/api/web/infos.get.ts`:
```ts
import type { Infos } from '~/types/master'
export default defineEventHandler(() => callMaster<Infos>('GET', '/get_infos'))
```

`server/api/web/servers.get.ts`:
```ts
import type { Server } from '~/types/master'
export default defineEventHandler(() => callMaster<Server[]>('GET', '/list_servers'))
```

`server/api/web/ladder.get.ts`:
```ts
import type { LadderRow } from '~/types/master'
export default defineEventHandler(() => callMaster<LadderRow[]>('GET', '/ladder'))
```

`server/api/web/tanks.get.ts`:
```ts
import type { Tank } from '~/types/master'
export default defineEventHandler(() => callMaster<Tank[]>('GET', '/get_tank_list'))
```

`server/api/web/profile.get.ts` (master expects query param `_idUser`; whitelist response):
```ts
import type { Profile } from '~/types/master'
export default defineEventHandler(async (event) => {
  const { id } = getQuery(event)
  if (!id) throw createError({ statusCode: 400, statusMessage: 'Missing id' })
  const user = await callMaster<any>('GET', '/user_profile', { query: { _idUser: id } })
  return whitelistProfile(user) as Profile
})
```

- [ ] **Step 3: Write the POST routes**

`server/api/web/register.post.ts`:
```ts
export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  await callMaster<boolean>('POST', '/register', {
    body: { username: body.username, email: body.email, password: body.password, from: body.from, createdAt: new Date() }
  })
  return { ok: true }
})
```

`server/api/web/login.post.ts`:
```ts
import type { LoginResult } from '~/types/master'
export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  const user = await callMaster<any>('POST', '/login', { body: { login: body.login, password: body.password } })
  return whitelistLogin(user) as LoginResult
})
```

`server/api/web/active.post.ts`:
```ts
export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  await callMaster<any>('POST', '/active_account', { body: { _idUser: body.id } })
  return { ok: true }
})
```

- [ ] **Step 4: Convert `MasterError` to an HTTP error (Nitro plugin)**

Create `server/plugins/error-map.ts`:
```ts
import { MasterError } from '~/server/utils/master'
export default defineNitroPlugin((nitro) => {
  nitro.hooks.hook('error', (error: any) => {
    // Surfacing only; mapping done per-handler below.
    void error
  })
})
```

Instead of a global hook, wrap throws at call sites: update `callMaster` is fine, but ensure handlers translate `MasterError`. Simplest: add a helper used by handlers. Replace the body of each handler's `callMaster` usage by catching:

Add to `server/utils/master.ts` (and re-run Task 2 tests — still pass, pure additions):
```ts
export function toHttpError(e: unknown) {
  if (e instanceof MasterError) return createError({ statusCode: e.statusCode, statusMessage: e.message })
  return createError({ statusCode: 502, statusMessage: 'Master server unreachable' })
}
```
Then each handler uses `try { ... } catch (e) { throw toHttpError(e) }`. Apply this wrapper to all 8 handlers. Example for `login.post.ts`:
```ts
import type { LoginResult } from '~/types/master'
export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  try {
    const user = await callMaster<any>('POST', '/login', { body: { login: body.login, password: body.password } })
    return whitelistLogin(user) as LoginResult
  } catch (e) { throw toHttpError(e) }
})
```
Apply the same `try/catch (e) { throw toHttpError(e) }` wrapper to the other 7 handlers. (Remove the unused `server/plugins/error-map.ts` stub created above.)

- [ ] **Step 5: Write the integration test `tests/unit/api-login.spec.ts`**

Uses `@nuxt/test-utils` to boot the app and mocks the upstream master via intercepting `$fetch`. Mock the master URL response shape.

```ts
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setup, $fetch } from '@nuxt/test-utils/e2e'

describe('POST /api/web/login', async () => {
  await setup({ server: true })

  it('whitelists fields on success', async () => {
    // intercept the upstream master call
    const spy = vi.spyOn(globalThis, '$fetch' as any).mockResolvedValue({
      name: 'login', res: { _id: '1', username: 'Bob', token: 'T', password: 'HASH' }, err: null
    })
    const res = await $fetch('/api/web/login', { method: 'POST', body: { login: 'Bob', password: 'x' } })
    expect(res).toEqual({ _id: '1', username: 'Bob', token: 'T' })
    spy.mockRestore()
  })
})
```

> Note: if `$fetch` interception proves flaky under `setup`, substitute a stub master via `API_URL=http://127.0.0.1:<port>` pointed at a tiny test server started in `beforeEach`. The assertion (whitelist strips `password`) is the contract that must hold.

- [ ] **Step 6: Run unit tests**

Run: `npm run test:unit`
Expected: PASS — login route returns `{ _id, username, token }`, no `password`.

- [ ] **Step 7: Commit**

```bash
git add WebTinyTank/server WebTinyTank/types WebTinyTank/tests/unit/api-login.spec.ts
git commit -m "feat(web): add 8 Nitro BFF routes proxying master /api/web with whitelist"
```

---

## Task 4: Composables — useAuth, usePolling, useAlerts (TDD where logic-bearing)

**Files:**
- Create: `composables/useAuth.ts`, `composables/usePolling.ts`, `composables/useAlerts.ts`
- Test: `tests/unit/useAuth.spec.ts`, `tests/unit/usePolling.spec.ts`

**Interfaces:**
- Produces:
  - `useAuth()` → `{ isConnected: Ref<boolean>, username: Ref<string|null>, authId: Ref<string|null>, setSession(r: {_id,username,token}), logout() }`. Backed by `localStorage` keys `authToken`/`authID`/`authUsername`; reactive across the app via `useState`.
  - `usePolling(fn: () => void | Promise<void>, ms: number)` → starts `fn()` immediately on mount, repeats every `ms`, clears on unmount. Returns `{ stop() }`.
  - `useAlerts()` → `{ alerts: Ref<Alert[]>, notify(message: string, title?: string, level?: 'warning'|'success'|'danger'), dismiss(id) }`. Auto-dismiss after 5000ms. `Alert = { id, message, title, level }`.

- [ ] **Step 1: Write failing tests**

`tests/unit/useAuth.spec.ts`:
```ts
import { describe, it, expect, beforeEach } from 'vitest'
import { useAuth } from '../../composables/useAuth'

beforeEach(() => localStorage.clear())

describe('useAuth', () => {
  it('starts disconnected', () => {
    const a = useAuth(); a.hydrate()
    expect(a.isConnected.value).toBe(false)
  })
  it('setSession stores token/id/username and connects', () => {
    const a = useAuth()
    a.setSession({ _id: '7', username: 'Zug', token: 'TOK' })
    expect(localStorage.getItem('authToken')).toBe('TOK')
    expect(localStorage.getItem('authID')).toBe('7')
    expect(localStorage.getItem('authUsername')).toBe('Zug')
    expect(a.isConnected.value).toBe(true)
    expect(a.username.value).toBe('Zug')
  })
  it('logout clears storage and disconnects', () => {
    const a = useAuth()
    a.setSession({ _id: '7', username: 'Zug', token: 'TOK' })
    a.logout()
    expect(localStorage.getItem('authToken')).toBeNull()
    expect(a.isConnected.value).toBe(false)
  })
})
```

`tests/unit/usePolling.spec.ts`:
```ts
import { describe, it, expect, vi } from 'vitest'

// usePolling relies on Vue lifecycle; test the bare interval contract via the exported factory
import { startPolling } from '../../composables/usePolling'

describe('startPolling', () => {
  it('calls fn immediately and on each interval, stop() clears it', () => {
    vi.useFakeTimers()
    const fn = vi.fn()
    const stop = startPolling(fn, 1000)
    expect(fn).toHaveBeenCalledTimes(1)        // immediate
    vi.advanceTimersByTime(2500)
    expect(fn).toHaveBeenCalledTimes(3)         // +2 ticks
    stop()
    vi.advanceTimersByTime(5000)
    expect(fn).toHaveBeenCalledTimes(3)         // no more
    vi.useRealTimers()
  })
})
```

- [ ] **Step 2: Run, confirm fail**

Run: `npm run test:unit`
Expected: FAIL — composables not implemented.

- [ ] **Step 3: Implement `composables/useAuth.ts`**

```ts
export function useAuth() {
  const isConnected = useState('auth:isConnected', () => false)
  const username = useState<string | null>('auth:username', () => null)
  const authId = useState<string | null>('auth:id', () => null)

  function hydrate() {
    if (import.meta.client) {
      const tok = localStorage.getItem('authToken')
      const id = localStorage.getItem('authID')
      isConnected.value = !!(tok && id)
      username.value = localStorage.getItem('authUsername')
      authId.value = id
    }
  }
  function setSession(r: { _id: string; username: string; token: string }) {
    localStorage.setItem('authToken', r.token)
    localStorage.setItem('authID', r._id)
    localStorage.setItem('authUsername', r.username)
    isConnected.value = true; username.value = r.username; authId.value = r._id
  }
  function logout() {
    localStorage.clear()
    isConnected.value = false; username.value = null; authId.value = null
  }
  return { isConnected, username, authId, hydrate, setSession, logout }
}
```

- [ ] **Step 4: Implement `composables/usePolling.ts`**

```ts
export function startPolling(fn: () => void | Promise<void>, ms: number) {
  fn()
  const id = setInterval(fn, ms)
  return () => clearInterval(id)
}

export function usePolling(fn: () => void | Promise<void>, ms: number) {
  let stop = () => {}
  onMounted(() => { stop = startPolling(fn, ms) })
  onUnmounted(() => stop())
  return { stop: () => stop() }
}
```

- [ ] **Step 5: Implement `composables/useAlerts.ts`**

```ts
export interface Alert { id: number; message: string; title: string; level: 'warning' | 'success' | 'danger' }
let counter = 0
export function useAlerts() {
  const alerts = useState<Alert[]>('alerts', () => [])
  function dismiss(id: number) { alerts.value = alerts.value.filter(a => a.id !== id) }
  function notify(message: string, title = '', level: Alert['level'] = 'warning') {
    if (!message) return
    const id = ++counter
    alerts.value = [...alerts.value, { id, message, title, level }]
    if (import.meta.client) setTimeout(() => dismiss(id), 5000)
  }
  return { alerts, notify, dismiss }
}
```

- [ ] **Step 6: Run tests, confirm pass**

Run: `npm run test:unit`
Expected: PASS (useAuth + startPolling specs green).

- [ ] **Step 7: Commit**

```bash
git add WebTinyTank/composables WebTinyTank/tests/unit/useAuth.spec.ts WebTinyTank/tests/unit/usePolling.spec.ts
git commit -m "feat(web): add useAuth, usePolling, useAlerts composables"
```

---

## Task 5: Typed API composable

**Files:**
- Create: `composables/useMasterApi.ts`

**Interfaces:**
- Consumes: types from `types/master.ts`.
- Produces: `useMasterApi()` → `{ getInfos(), getServers(), getLadder(), getProfile(id), getTanks(), register(data), login(data), active(id) }`, each a typed `$fetch` to the matching `/api/web/*` route.

- [ ] **Step 1: Implement `composables/useMasterApi.ts`**

```ts
import type { Infos, Server, LadderRow, Profile, Tank, LoginResult } from '~/types/master'

export function useMasterApi() {
  return {
    getInfos: () => $fetch<Infos>('/api/web/infos'),
    getServers: () => $fetch<Server[]>('/api/web/servers'),
    getLadder: () => $fetch<LadderRow[]>('/api/web/ladder'),
    getProfile: (id: string) => $fetch<Profile>('/api/web/profile', { query: { id } }),
    getTanks: () => $fetch<Tank[]>('/api/web/tanks'),
    register: (data: { username: string; email: string; password: string; from: string }) =>
      $fetch<{ ok: true }>('/api/web/register', { method: 'POST', body: data }),
    login: (data: { login: string; password: string }) =>
      $fetch<LoginResult>('/api/web/login', { method: 'POST', body: data }),
    active: (id: string) => $fetch<{ ok: true }>('/api/web/active', { method: 'POST', body: { id } })
  }
}
```

- [ ] **Step 2: Typecheck**

Run: `npx nuxi typecheck`
Expected: no type errors in `useMasterApi.ts`.

- [ ] **Step 3: Commit**

```bash
git add WebTinyTank/composables/useMasterApi.ts
git commit -m "feat(web): add typed useMasterApi composable"
```

---

## Task 6: Layouts + shell components (header, footer, alerts, search)

**Legacy reference:** `layout.html`, `full-layout.html`, `header.html` (nav brand → home; left: connected→username/logout else Login/Register; always Ladder/Servers List/About; center search; right Download + GitHub), `footer.html` (3 columns Website/Contact/Contributing), `error-item.html` (alert-{level} dismissible, `{{{message}}}` is HTML), `search.html`.

**Files:**
- Create: `layouts/default.vue`, `layouts/full.vue`, `components/AppHeader.vue`, `components/AppFooter.vue`, `components/AlertStack.vue`, `components/SearchBox.vue`
- Test: `tests/e2e/shell.spec.ts`

**Interfaces:**
- Consumes: `useAuth`, `useAlerts`.
- Produces: `default` layout (header affix=0, container, AlertStack, slot, footer); `full` layout (banner img, header affix=1, container-fluid, AlertStack, slot, footer). `AppHeader` shows username + logout when connected (client-resolved via `useAuth().hydrate()` in `onMounted`).

- [ ] **Step 1: Implement `components/AlertStack.vue`** (Bootstrap-class compatible markup; `message` rendered as HTML via `v-html` to match legacy `{{{message}}}`)

```vue
<script setup lang="ts">
const { alerts, dismiss } = useAlerts()
</script>
<template>
  <div class="container" id="errors">
    <div v-for="a in alerts" :key="a.id" :class="`alert alert-${a.level} alert-dismissible`" role="alert">
      <button type="button" class="close float-right" aria-label="Close" @click="dismiss(a.id)">
        <span aria-hidden="true">&times;</span>
      </button>
      <strong>{{ a.title }}</strong> <span v-html="a.message" />
    </div>
  </div>
</template>
```

- [ ] **Step 2: Implement `components/SearchBox.vue`** (submit → `/profile/<query>`; legacy tank-detection was a TODO, profile-only)

```vue
<script setup lang="ts">
const query = ref('')
function submit() { if (query.value) navigateTo(`/profile/${encodeURIComponent(query.value)}`) }
</script>
<template>
  <form class="text-center" role="search" @submit.prevent="submit">
    <div class="form-group"><div class="input-group">
      <span class="input-group-addon"><i class="glyphicon glyphicon-search" /></span>
      <input v-model="query" type="text" class="form-control" name="search" placeholder="Search" />
    </div></div>
    <button type="submit" class="btn btn-primary">Search</button>
  </form>
</template>
```

- [ ] **Step 3: Implement `components/AppHeader.vue`** (port `header.html` nav; auth state client-resolved)

```vue
<script setup lang="ts">
const auth = useAuth()
onMounted(() => auth.hydrate())
function onLogout() { auth.logout(); navigateTo('/') }
</script>
<template>
  <nav class="navbar navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <NuxtLink class="navbar-brand" to="/">TinyTank</NuxtLink>
      </div>
      <div class="navbar-collapse">
        <ul class="nav navbar-nav">
          <template v-if="auth.isConnected.value">
            <li><NuxtLink to="/profile">{{ auth.username.value }}</NuxtLink></li>
            <li><a id="logout" title="Logout" style="cursor:pointer" @click="onLogout"><i class="glyphicon glyphicon-off" /></a></li>
          </template>
          <li v-else><NuxtLink to="/login">Login/Register</NuxtLink></li>
          <li><NuxtLink to="/ladder">Ladder</NuxtLink></li>
          <li><NuxtLink to="/servers-list">Servers List</NuxtLink></li>
          <li><NuxtLink to="/about">About</NuxtLink></li>
        </ul>
        <div class="navbar-form navbar-right"><SearchBox /></div>
        <ul class="nav navbar-nav navbar-right">
          <li><NuxtLink to="/download">Download</NuxtLink></li>
          <li><a href="https://github.com/LeNiglo/TinyTank" target="_blank">GitHub</a></li>
        </ul>
      </div>
    </div>
  </nav>
</template>
```

- [ ] **Step 4: Implement `components/AppFooter.vue`** (port `footer.html` — 3 columns; about anchors via `/about#donate` etc.)

```vue
<template>
  <footer class="container-fluid">
    <hr />
    <div class="container text-center"><div class="row">
      <div class="col-md-4">
        <h2 class="page-information">Website</h2>
        <ul class="list-unstyled">
          <li><NuxtLink to="/">Home</NuxtLink></li>
          <li><NuxtLink to="/login">Login / Register</NuxtLink></li>
          <li><NuxtLink to="/about">About</NuxtLink></li>
          <li><NuxtLink to="/download">Download</NuxtLink></li>
        </ul>
      </div>
      <div class="col-md-4">
        <h2 class="page-information">Contact</h2>
        <ul class="list-unstyled">
          <li><a href="mailto:lefrantguillaume@gmail.com">Lefrant Guillaume</a></li>
          <li><a href="mailto:kevin.draym@gmail.com">Kévin Andres</a></li>
          <li><a href="mailto:styve.simonneau@gmail.com">Styve Simonneau</a></li>
          <li><a href="mailto:alexandre.quintin@hotmail.fr">Alexandre Quintin</a></li>
        </ul>
      </div>
      <div class="col-md-4">
        <h2 class="page-information">Contributing</h2>
        <ul class="list-unstyled">
          <li><NuxtLink to="/about#donate">Donate</NuxtLink></li>
          <li><NuxtLink to="/about#content">Content</NuxtLink></li>
          <li><NuxtLink to="/about#development">Development</NuxtLink></li>
        </ul>
      </div>
    </div></div>
  </footer>
</template>
```

- [ ] **Step 5: Implement layouts**

`layouts/default.vue`:
```vue
<template>
  <div>
    <AppHeader />
    <AlertStack />
    <div class="container"><slot /></div>
    <AppFooter />
  </div>
</template>
```

`layouts/full.vue`:
```vue
<template>
  <div>
    <img src="/img/banner.png" alt="banner" style="width:100%" class="img-responsive" />
    <AppHeader />
    <div class="container-fluid">
      <AlertStack />
      <slot />
    </div>
    <AppFooter />
  </div>
</template>
```

- [ ] **Step 6: Write `tests/e2e/shell.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('header shows Login when logged out, nav links present', async ({ page }) => {
  await page.goto('/login')
  await expect(page.getByRole('link', { name: 'Login/Register' })).toBeVisible()
  await expect(page.getByRole('link', { name: 'Ladder' })).toBeVisible()
  await expect(page.getByRole('link', { name: 'Servers List' })).toBeVisible()
  await expect(page.getByRole('link', { name: 'Download' })).toBeVisible()
})

test('search box navigates to profile', async ({ page }) => {
  await page.route('**/api/web/profile**', r => r.fulfill({ json: { _id: 'x', username: 'x', from: '', createdAt: '', updatedAt: '', stats: { gamesPlayed: 0, kills: 0, deaths: 0, score: 0, shotsFired: 0, shotsHit: 0, killsPG: 0, deathsPG: 0, scorePG: 0, shotsFiredPG: 0, shotsHitPG: 0 } } } }))
  await page.goto('/login')
  await page.fill('input[name="search"]', 'Bob')
  await page.getByRole('button', { name: 'Search' }).click()
  await expect(page).toHaveURL(/\/profile\/Bob/)
})

test('logged-in header shows username + logout', async ({ page }) => {
  await page.addInitScript(() => {
    localStorage.setItem('authToken', 'T'); localStorage.setItem('authID', '7'); localStorage.setItem('authUsername', 'Zug')
  })
  await page.goto('/about')
  await expect(page.getByRole('link', { name: 'Zug' })).toBeVisible()
  await expect(page.locator('#logout')).toBeVisible()
})
```

- [ ] **Step 7: Run E2E**

Run: `npm run test:e2e -- shell.spec.ts`
Expected: PASS (3 tests).

- [ ] **Step 8: Commit**

```bash
git add WebTinyTank/layouts WebTinyTank/components WebTinyTank/tests/e2e/shell.spec.ts
git commit -m "feat(web): add layouts + header/footer/alerts/search shell"
```

---

## Task 7: Home page (`/`) + GlobalInfos + Paypal + banner message rotation

**Legacy reference:** `home.html` (h1 "Tiny Tank Official", h2 `<span id="tinyHomeMessage"></span>, on Tiny Tank.`, TinyBanner.png, 3 sections — "Fun Comes First" w/ mod list FFA/TDM/KTH/DTB, "Rankable gaming", "Growing Community" w/ infos + paypal). `home.js` rotates messages `["Have Fun","Get Blasted","Rank Up"]` colors `["#7F0000","#CC0000","#7F2626"]` every 3s. `infos.html`/`infos.js`: "{n} registered users." and "Greetings to <user>. Joined us {fromNow}", polled 120s. `paypal.html`: hosted_button_id WS2DP8TNEQSW8, donate.jpg.

**Files:**
- Create: `pages/index.vue`, `components/HomeBanner.vue`, `components/GlobalInfos.vue`, `components/PaypalButton.vue`
- Test: `tests/e2e/home.spec.ts`

**Interfaces:**
- Consumes: `useMasterApi().getInfos`, `usePolling`, relative-time formatting (use native `Intl.RelativeTimeFormat` helper `composables/useTimeAgo.ts` — see step 1).

- [ ] **Step 1: Add `composables/useTimeAgo.ts`** (replaces moment `fromNow`)

```ts
export function timeAgo(input: string | number | Date): string {
  const d = new Date(input).getTime()
  if (Number.isNaN(d)) return ''
  const diff = Math.round((d - Date.now()) / 1000)
  const rtf = new Intl.RelativeTimeFormat('en', { numeric: 'auto' })
  const units: [Intl.RelativeTimeFormatUnit, number][] = [['year',31536000],['month',2592000],['day',86400],['hour',3600],['minute',60],['second',1]]
  for (const [unit, secs] of units) {
    if (Math.abs(diff) >= secs || unit === 'second') return rtf.format(Math.round(diff / secs), unit)
  }
  return ''
}
```

- [ ] **Step 2: Implement `components/PaypalButton.vue`** (verbatim port)

```vue
<template>
  <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
    <input type="hidden" name="cmd" value="_s-xclick" />
    <input type="hidden" name="hosted_button_id" value="WS2DP8TNEQSW8" />
    <input type="image" src="/img/donate.jpg" border="0" name="submit" alt="PayPal – The safer, easier way to pay online." style="height:120px" />
  </form>
</template>
```

- [ ] **Step 3: Implement `components/GlobalInfos.vue`** (polls infos 120s)

```vue
<script setup lang="ts">
import type { Infos } from '~/types/master'
const api = useMasterApi()
const infos = ref<Infos | null>(null)
async function refresh() { try { infos.value = await api.getInfos() } catch {} }
usePolling(refresh, 120_000)
</script>
<template>
  <div>
    <p>{{ infos?.nb_users ?? 0 }} registered users.</p>
    <p v-if="infos?.last">
      Greetings to <NuxtLink :to="`/profile/${infos.last._id}`">{{ infos.last.username }}</NuxtLink>.
      Joined us {{ timeAgo(infos.last.createdAt) }}
    </p>
  </div>
</template>
```

- [ ] **Step 4: Implement `components/HomeBanner.vue`** (shuffle-letters effect optional; rotate message + color every 3s)

```vue
<script setup lang="ts">
const messages = ['Have Fun', 'Get Blasted', 'Rank Up']
const colors = ['#7F0000', '#CC0000', '#7F2626']
const idx = ref(0)
let timer: any
onMounted(() => { timer = setInterval(() => { idx.value = (idx.value + 1) % messages.length }, 3000) })
onUnmounted(() => clearInterval(timer))
</script>
<template>
  <h2 class="text-primary text-center">
    <span id="tinyHomeMessage" :style="{ color: colors[idx] }">{{ messages[idx] }}</span>, on Tiny Tank.
  </h2>
</template>
```

- [ ] **Step 5: Implement `pages/index.vue`** (full layout; port `home.html` sections)

```vue
<script setup lang="ts">
definePageMeta({ layout: 'full' })
useHead({ title: 'TinyTank' })
</script>
<template>
  <div>
    <h1>Tiny Tank Official</h1>
    <HomeBanner />
    <div id="home">
      <section>
        <img src="/img/TinyBanner.png" class="img-responsive" style="margin:auto" />
        <div class="container text-center">
          <h2 class="page-header">Fun Comes First</h2>
          <p>Our cute tiny tanks will make you enjoy every moment of gaming. Play in a lot of mods :</p>
          <ul class="list-unstyled">
            <li>Free For All <strong>[FFA]</strong></li>
            <li>Team DeathMatch <strong>[TDM]</strong></li>
            <li>King of the Hill <strong>[KTH]</strong></li>
            <li>Drop The Bomb <strong>[DTB]</strong></li>
          </ul>
        </div>
      </section>
      <section><div class="container text-center">
        <h2 class="page-header">Rankable gaming</h2>
        <p>A system of ladder is included so playing with your friends can become a competitive match.</p>
        <p>You can also gain medals by playing some mod and reaching some score for example. Those medals will be visible by anyone who is looking at your profile.</p>
      </div></section>
      <section><div class="container text-center">
        <h2 class="page-header">Growing Community</h2>
        <div class="col-md-6"><p>Join our growing army !</p><GlobalInfos /></div>
        <div class="col-md-6"><PaypalButton /></div>
      </div></section>
    </div>
  </div>
</template>
```

- [ ] **Step 6: Write `tests/e2e/home.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('home renders sections, banner, and polled infos', async ({ page }) => {
  await page.route('**/api/web/infos', r => r.fulfill({ json: { last: { username: 'Zug', _id: '7', createdAt: new Date().toISOString() }, nb_users: 42 } }))
  await page.goto('/')
  await expect(page.getByRole('heading', { name: 'Tiny Tank Official' })).toBeVisible()
  await expect(page.getByText('Fun Comes First')).toBeVisible()
  await expect(page.locator('img[src="/img/banner.png"]')).toBeVisible()       // full layout
  await expect(page.getByText('42 registered users.')).toBeVisible()
  await expect(page.getByRole('link', { name: 'Zug' })).toBeVisible()
})

test('banner message rotates', async ({ page }) => {
  await page.route('**/api/web/infos', r => r.fulfill({ json: { last: null, nb_users: 0 } }))
  await page.goto('/')
  await expect(page.locator('#tinyHomeMessage')).toHaveText('Have Fun')
  await expect(page.locator('#tinyHomeMessage')).toHaveText('Get Blasted', { timeout: 4000 })
})
```

- [ ] **Step 7: Run E2E**

Run: `npm run test:e2e -- home.spec.ts`
Expected: PASS (2 tests).

- [ ] **Step 8: Commit**

```bash
git add WebTinyTank/pages/index.vue WebTinyTank/components/HomeBanner.vue WebTinyTank/components/GlobalInfos.vue WebTinyTank/components/PaypalButton.vue WebTinyTank/composables/useTimeAgo.ts WebTinyTank/tests/e2e/home.spec.ts
git commit -m "feat(web): home page with banner rotation, polled infos, paypal"
```

---

## Task 8: Ladder page (`/ladder`)

**Legacy reference:** `ladder.html` table cols Rank/Username/Games Played/Kill Count/Accuracy %/medal; rows from `getLadder`. `ladder.js`: poll 60s; top-3 styling — rank1 gold star in last cell; font-size `28-3*i` px; rank-cell color ramp `#F15841/#A83A2A/#4D241E`.

**Files:**
- Create: `pages/ladder.vue`, `components/LadderTable.vue`
- Test: `tests/e2e/ladder.spec.ts`

**Interfaces:**
- Consumes: `useMasterApi().getLadder`, `usePolling`.

- [ ] **Step 1: Implement `components/LadderTable.vue`**

```vue
<script setup lang="ts">
import type { LadderRow } from '~/types/master'
defineProps<{ rows: LadderRow[] }>()
const colors = ['#F15841', '#A83A2A', '#4D241E']
function rowStyle(i: number) { return i < 3 ? { fontSize: `${28 - 3 * i}px` } : {} }
function rankStyle(i: number) { return i < 3 ? { color: colors[i] } : {} }
</script>
<template>
  <table class="table table-hover" id="ladder" style="table-layout:fixed">
    <thead><tr><th>Rank</th><th>Username</th><th>Games Played</th><th>Kill Count</th><th>Accuracy</th><th /></tr></thead>
    <tbody>
      <tr v-for="(row, i) in rows" :key="row._id" :style="rowStyle(i)">
        <th class="rank" :style="rankStyle(i)">{{ row.rank }}</th>
        <td class="username"><NuxtLink :to="`/profile/${row._id}`">{{ row.username }}</NuxtLink></td>
        <td>{{ row.gamesPlayed }}</td>
        <td>{{ row.killCount }}</td>
        <td>{{ row.accuracy }} %</td>
        <td class="medal"><i v-if="i === 0" class="glyphicon glyphicon-star" style="color:gold" /></td>
      </tr>
    </tbody>
  </table>
</template>
```

- [ ] **Step 2: Implement `pages/ladder.vue`**

```vue
<script setup lang="ts">
import type { LadderRow } from '~/types/master'
useHead({ title: 'Ladder — TinyTank' })
const api = useMasterApi()
const rows = ref<LadderRow[]>([])
async function refresh() { try { rows.value = await api.getLadder() } catch {} }
usePolling(refresh, 60_000)
</script>
<template>
  <div><h1>Ladder</h1><LadderTable :rows="rows" /></div>
</template>
```

- [ ] **Step 3: Write `tests/e2e/ladder.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

const LADDER = [
  { rank: 1, username: 'LeNiglo', _id: 'a', gamesPlayed: 1337, killCount: 3214, accuracy: 85.2 },
  { rank: 2, username: 'Switi', _id: 'b', gamesPlayed: 1302, killCount: 729, accuracy: 87.8 },
  { rank: 3, username: 'DraymZz', _id: 'c', gamesPlayed: 668, killCount: 520, accuracy: 83.0 }
]

test('ladder renders rows and top-1 gold star', async ({ page }) => {
  await page.route('**/api/web/ladder', r => r.fulfill({ json: LADDER }))
  await page.goto('/ladder')
  await expect(page.getByRole('link', { name: 'LeNiglo' })).toBeVisible()
  await expect(page.locator('#ladder tbody tr')).toHaveCount(3)
  await expect(page.locator('#ladder tbody tr').first().locator('.medal .glyphicon-star')).toBeVisible()
  await expect(page.getByText('85.2 %')).toBeVisible()
})
```

- [ ] **Step 4: Run E2E** — `npm run test:e2e -- ladder.spec.ts` — Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/pages/ladder.vue WebTinyTank/components/LadderTable.vue WebTinyTank/tests/e2e/ladder.spec.ts
git commit -m "feat(web): ladder page with 60s polling and top-3 styling"
```

---

## Task 9: Servers list page (`/servers-list`)

**Legacy reference:** `servers-list.html` cols Server Name/Current Map/Uptime/Nb Players; `servers-list.js` poll 60s; uptime = `fromNow(started_at)`; player count = `users.length`.

**Files:**
- Create: `pages/servers-list.vue`, `components/ServerList.vue`
- Test: `tests/e2e/servers.spec.ts`

- [ ] **Step 1: Implement `components/ServerList.vue`**

```vue
<script setup lang="ts">
import type { Server } from '~/types/master'
defineProps<{ servers: Server[] }>()
</script>
<template>
  <table class="table">
    <thead><tr><th>Server Name</th><th>Current Map</th><th>Uptime</th><th>Nb Players</th></tr></thead>
    <tbody>
      <tr v-for="s in servers" :key="s._id">
        <th>{{ s.name }}</th><td>{{ s.map }}</td><td>{{ timeAgo(s.started_at) }}</td><td>{{ s.users.length }}</td>
      </tr>
    </tbody>
  </table>
</template>
```

- [ ] **Step 2: Implement `pages/servers-list.vue`**

```vue
<script setup lang="ts">
import type { Server } from '~/types/master'
useHead({ title: 'Servers List — TinyTank' })
const api = useMasterApi()
const servers = ref<Server[]>([])
async function refresh() { try { servers.value = await api.getServers() } catch {} }
usePolling(refresh, 60_000)
</script>
<template>
  <div><h1>Servers List</h1><ServerList :servers="servers" /></div>
</template>
```

- [ ] **Step 3: Write `tests/e2e/servers.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('servers list renders rows with player count', async ({ page }) => {
  await page.route('**/api/web/servers', r => r.fulfill({ json: [
    { _id: 's1', name: 'EU-1', ip: '1.2.3.4', ports: { udp: 1, tcp: 2 }, users: ['a', 'b'], map: 'desert', started_at: new Date().toISOString(), last_active: new Date().toISOString() }
  ] }))
  await page.goto('/servers-list')
  await expect(page.getByText('EU-1')).toBeVisible()
  await expect(page.getByText('desert')).toBeVisible()
  await expect(page.locator('table tbody tr td').last()).toHaveText('2')
})
```

- [ ] **Step 4: Run E2E** — `npm run test:e2e -- servers.spec.ts` — Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/pages/servers-list.vue WebTinyTank/components/ServerList.vue WebTinyTank/tests/e2e/servers.spec.ts
git commit -m "feat(web): servers-list page with 60s polling"
```

---

## Task 10: Profile page (`/profile/:id?`)

**Legacy reference:** `profile.js` — id defaults to `localStorage.authID`; none → redirect `/login`; title `<username>'s profile on TinyTank`; not found → alert + show search. `profile.html` — loading state; `profileDetail`: "Registered {fromNow} from {from}", "Last seen {fromNow updatedAt}", table Kills/Deaths/Score with `(/g)` per-game, Accuracy = `shotsHit*100/shotsFired` `.toFixed(2)`.

**Files:**
- Create: `pages/profile/[[id]].vue`, `components/ProfileCard.vue`
- Test: `tests/e2e/profile.spec.ts`

**Interfaces:**
- Consumes: `useMasterApi().getProfile`, `useAuth`, `timeAgo`.

- [ ] **Step 1: Implement `components/ProfileCard.vue`**

```vue
<script setup lang="ts">
import type { Profile } from '~/types/master'
const props = defineProps<{ profile: Profile }>()
const accuracy = computed(() => {
  const s = props.profile.stats
  return s.shotsFired ? (s.shotsHit * 100 / s.shotsFired).toFixed(2) : '0.00'
})
</script>
<template>
  <div>
    <h2 class="page-header text-center">{{ profile.username }}</h2>
    <p>Registered {{ timeAgo(profile.createdAt) }} from {{ profile.from }}</p>
    <p>Last seen {{ timeAgo(profile.updatedAt) }}</p>
    <table class="table"><tbody>
      <tr><th>Kills</th><td>{{ profile.stats.kills }} ({{ profile.stats.killsPG }}/g)</td></tr>
      <tr><th>Deaths</th><td>{{ profile.stats.deaths }} ({{ profile.stats.deathsPG }}/g)</td></tr>
      <tr><th>Score</th><td>{{ profile.stats.score }} ({{ profile.stats.scorePG }}/g)</td></tr>
      <tr><th>Accuracy</th><td>{{ accuracy }} %</td></tr>
    </tbody></table>
  </div>
</template>
```

- [ ] **Step 2: Implement `pages/profile/[[id]].vue`** (client-resolves id from localStorage when absent)

```vue
<script setup lang="ts">
import type { Profile } from '~/types/master'
const route = useRoute()
const api = useMasterApi()
const auth = useAuth()
const { notify } = useAlerts()
const profile = ref<Profile | null>(null)
const loading = ref(true)
const notFound = ref(false)

async function load() {
  let id = (route.params.id as string) || ''
  if (!id) { auth.hydrate(); id = auth.authId.value || '' }
  if (!id) { return navigateTo('/login') }
  loading.value = true; notFound.value = false
  try {
    profile.value = await api.getProfile(id)
    useHead({ title: `${profile.value.username}'s profile on TinyTank` })
  } catch (e: any) {
    profile.value = null; notFound.value = true
    notify('Maybe you mispelled it.', 'User not found', 'danger')
  } finally { loading.value = false }
}
onMounted(load)
watch(() => route.params.id, load)
</script>
<template>
  <div class="container">
    <h2 v-if="loading" class="page-header text-center">Loading ...</h2>
    <ProfileCard v-else-if="profile" :profile="profile" />
    <template v-else>
      <h2 class="page-header text-center">User Not Found.</h2>
      <SearchBox />
    </template>
  </div>
</template>
```

- [ ] **Step 3: Write `tests/e2e/profile.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

const PROFILE = { _id: '7', username: 'Zug', from: 'France', createdAt: new Date(Date.now() - 86400000).toISOString(), updatedAt: new Date().toISOString(), stats: { gamesPlayed: 2, kills: 10, deaths: 4, score: 50, shotsFired: 100, shotsHit: 73, killsPG: 5, deathsPG: 2, scorePG: 25, shotsFiredPG: 50, shotsHitPG: 36.5 } }

test('profile by id renders stats and accuracy', async ({ page }) => {
  await page.route('**/api/web/profile**', r => r.fulfill({ json: PROFILE }))
  await page.goto('/profile/7')
  await expect(page.getByRole('heading', { name: 'Zug' })).toBeVisible()
  await expect(page.getByText('73.00 %')).toBeVisible()   // 73/100*100
})

test('profile without id and no auth redirects to login', async ({ page }) => {
  await page.goto('/profile')
  await expect(page).toHaveURL(/\/login/)
})

test('unknown profile shows Not Found + search', async ({ page }) => {
  await page.route('**/api/web/profile**', r => r.fulfill({ status: 400, json: { statusCode: 400, message: 'User not found.' } }))
  await page.goto('/profile/nobody')
  await expect(page.getByText('User Not Found.')).toBeVisible()
  await expect(page.locator('input[name="search"]')).toBeVisible()
})
```

- [ ] **Step 4: Run E2E** — `npm run test:e2e -- profile.spec.ts` — Expected: PASS (3 tests).

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/pages/profile WebTinyTank/components/ProfileCard.vue WebTinyTank/tests/e2e/profile.spec.ts
git commit -m "feat(web): profile page with id/localStorage resolution and not-found"
```

---

## Task 11: Tank page (`/tank/:id?`)

**Note:** the legacy app declared a `tank` route but shipped **no tank template** (net-new). Build a minimal catalog page from `getTankList`: list tanks; if `:id` present, show that tank's details. Keep it simple and faithful to the data shape (tank docs are opaque key/values).

**Files:**
- Create: `pages/tank/[[id]].vue`, `components/TankCard.vue`
- Test: `tests/e2e/tank.spec.ts`

- [ ] **Step 1: Implement `components/TankCard.vue`**

```vue
<script setup lang="ts">
import type { Tank } from '~/types/master'
defineProps<{ tank: Tank }>()
</script>
<template>
  <div class="tank-card">
    <h3>{{ tank.name ?? tank._id }}</h3>
    <table class="table"><tbody>
      <tr v-for="(value, key) in tank" :key="key"><th>{{ key }}</th><td>{{ value }}</td></tr>
    </tbody></table>
  </div>
</template>
```

- [ ] **Step 2: Implement `pages/tank/[[id]].vue`**

```vue
<script setup lang="ts">
import type { Tank } from '~/types/master'
useHead({ title: 'Tanks — TinyTank' })
const route = useRoute()
const api = useMasterApi()
const tanks = ref<Tank[]>([])
onMounted(async () => { try { tanks.value = await api.getTanks() } catch {} })
const selected = computed(() => route.params.id ? tanks.value.find(t => t._id === route.params.id || (t as any).name === route.params.id) : null)
</script>
<template>
  <div>
    <h1>Tanks</h1>
    <TankCard v-if="selected" :tank="selected" />
    <div v-else class="row">
      <div v-for="t in tanks" :key="t._id" class="col-md-4">
        <NuxtLink :to="`/tank/${t._id}`">{{ (t as any).name ?? t._id }}</NuxtLink>
      </div>
    </div>
  </div>
</template>
```

- [ ] **Step 3: Write `tests/e2e/tank.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('tank list renders from catalog', async ({ page }) => {
  await page.route('**/api/web/tanks', r => r.fulfill({ json: [{ _id: 't1', name: 'Sherman' }, { _id: 't2', name: 'Tiger' }] }))
  await page.goto('/tank')
  await expect(page.getByRole('link', { name: 'Sherman' })).toBeVisible()
  await expect(page.getByRole('link', { name: 'Tiger' })).toBeVisible()
})

test('tank detail shows selected tank', async ({ page }) => {
  await page.route('**/api/web/tanks', r => r.fulfill({ json: [{ _id: 't1', name: 'Sherman', hp: 100 }] }))
  await page.goto('/tank/t1')
  await expect(page.getByRole('heading', { name: 'Sherman' })).toBeVisible()
  await expect(page.getByText('hp')).toBeVisible()
})
```

- [ ] **Step 4: Run E2E** — `npm run test:e2e -- tank.spec.ts` — Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/pages/tank WebTinyTank/components/TankCard.vue WebTinyTank/tests/e2e/tank.spec.ts
git commit -m "feat(web): tank catalog page from getTankList (net-new)"
```

---

## Task 12: Login page (`/login`)

**Legacy reference:** `login.html` form (email/username + password, forgot link, Login + Register buttons). `login.js`: require both fields; call login; on `err` danger alert; on success store token/id/username, redirect `/profile`, welcome alert.

**Files:**
- Create: `pages/login.vue`
- Test: `tests/e2e/login.spec.ts`

**Interfaces:**
- Consumes: `useMasterApi().login`, `useAuth().setSession`, `useAlerts`.

- [ ] **Step 1: Implement `pages/login.vue`**

```vue
<script setup lang="ts">
useHead({ title: 'Login — TinyTank' })
const api = useMasterApi()
const auth = useAuth()
const { notify } = useAlerts()
const email = ref(''); const password = ref('')
async function submit() {
  if (!email.value || !password.value) return
  try {
    const r = await api.login({ login: email.value, password: password.value })
    auth.setSession(r)
    await navigateTo('/profile')
    notify(`Happy to see you, ${r.username} !`, 'Welcome Back !', 'success')
  } catch (e: any) {
    notify(e?.data?.message || e?.message || 'Login failed', 'Login failed, ', 'danger')
  }
}
</script>
<template>
  <div class="col-md-6 col-md-offset-3 text-center">
    <h2 class="page-header">Login</h2>
    <form id="login" role="login" @submit.prevent="submit">
      <div class="form-group"><input v-model="email" type="text" name="email" class="form-control" placeholder="email or username" required /></div>
      <div class="form-group">
        <input v-model="password" type="password" name="password" class="form-control" placeholder="password" required />
        <small class="text-right"><NuxtLink to="/forgot-password">Forgot password ?</NuxtLink></small>
      </div>
      <div class="form-group row text-center">
        <div class="col-md-6"><input type="submit" class="btn btn-success form-control" value="Login to your Account" /></div>
        <div class="col-md-6"><NuxtLink to="/register" class="btn btn-warning form-control">Register</NuxtLink></div>
      </div>
    </form>
  </div>
</template>
```

- [ ] **Step 2: Write `tests/e2e/login.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('successful login stores session and redirects to profile', async ({ page }) => {
  await page.route('**/api/web/login', r => r.fulfill({ json: { _id: '7', username: 'Zug', token: 'TOK' } }))
  await page.route('**/api/web/profile**', r => r.fulfill({ json: { _id: '7', username: 'Zug', from: '', createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(), stats: { gamesPlayed: 0, kills: 0, deaths: 0, score: 0, shotsFired: 0, shotsHit: 0, killsPG: 0, deathsPG: 0, scorePG: 0, shotsFiredPG: 0, shotsHitPG: 0 } } }))
  await page.goto('/login')
  await page.fill('input[name="email"]', 'Zug')
  await page.fill('input[name="password"]', 'secret')
  await page.getByRole('button', { name: 'Login to your Account' }).click()
  await expect(page).toHaveURL(/\/profile/)
  expect(await page.evaluate(() => localStorage.getItem('authToken'))).toBe('TOK')
  await expect(page.getByText('Welcome Back !')).toBeVisible()
})

test('failed login shows danger alert', async ({ page }) => {
  await page.route('**/api/web/login', r => r.fulfill({ status: 400, json: { statusCode: 400, message: 'Account not active.' } }))
  await page.goto('/login')
  await page.fill('input[name="email"]', 'Zug')
  await page.fill('input[name="password"]', 'bad')
  await page.getByRole('button', { name: 'Login to your Account' }).click()
  await expect(page.getByText('Account not active.')).toBeVisible()
})
```

- [ ] **Step 3: Run E2E** — `npm run test:e2e -- login.spec.ts` — Expected: PASS (2 tests).

- [ ] **Step 4: Commit**

```bash
git add WebTinyTank/pages/login.vue WebTinyTank/tests/e2e/login.spec.ts
git commit -m "feat(web): login page with session storage and alerts"
```

---

## Task 13: Register page (`/register`)

**Legacy reference:** `register.html` (email, username, password + confirm, country select, submit, "Already an account?"). `register.js`: passwords must match; regexes — email, password `^[\W\w]{7,99}$`, username `^[\w\s]{3,20}$`; live password-confirm match indicator; on success redirect `/login` + "activation email sent" success alert; countries `France, United Kingdom, Spain, Deutschland, Belgium, United States, Other`.

**Files:**
- Create: `pages/register.vue`
- Test: `tests/e2e/register.spec.ts`

- [ ] **Step 1: Implement `pages/register.vue`**

```vue
<script setup lang="ts">
useHead({ title: 'Register — TinyTank' })
const api = useMasterApi()
const { notify } = useAlerts()
const countries = ['France', 'United Kingdom', 'Spain', 'Deutschland', 'Belgium', 'United States', 'Other']
const email = ref(''); const username = ref(''); const password = ref(''); const passwordV = ref(''); const from = ref('France')
const emailRe = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
const passwordRe = /^[\W\w]{7,99}$/
const usernameRe = /^[\w\s]{3,20}$/
const matchOk = computed(() => password.value.length > 0 && password.value === passwordV.value)
async function submit() {
  if (password.value !== passwordV.value) return notify('Passwords must match.', 'Attention,', 'warning')
  if (!(emailRe.test(email.value) && passwordRe.test(password.value) && usernameRe.test(username.value)))
    return notify("Parameters aren't correct, please try again with others.", 'Not enought strength.', 'danger')
  try {
    await api.register({ username: username.value, email: email.value, password: password.value, from: from.value })
    await navigateTo('/login')
    notify('An activation email has been sent to you. (required)', 'Congratulations !', 'success')
  } catch (e: any) { notify(e?.data?.message || e?.message || 'Registration failed', '', 'danger') }
}
</script>
<template>
  <div class="col-md-6 col-md-offset-3 text-center">
    <h2 class="page-header">Register</h2>
    <form id="register" role="register" @submit.prevent="submit">
      <div class="form-group"><input v-model="email" type="email" name="email" class="form-control" placeholder="email" required /></div>
      <div class="form-group"><input v-model="username" type="text" name="username" class="form-control" placeholder="username" required /></div>
      <div class="form-group" :class="{ 'has-success': matchOk }">
        <input v-model="password" type="password" name="password" class="form-control" placeholder="password" required />
        <input v-model="passwordV" type="password" name="password-v" class="form-control" placeholder="confirm" required />
      </div>
      <div class="form-group">
        <select v-model="from" name="from" class="form-control">
          <option v-for="c in countries" :key="c" :value="c">{{ c }}</option>
        </select>
      </div>
      <div class="form-group text-center"><input type="submit" class="btn btn-success form-control" value="Register" /></div>
      <NuxtLink to="/login">Already an account ?</NuxtLink>
    </form>
  </div>
</template>
```

- [ ] **Step 2: Write `tests/e2e/register.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('mismatched passwords warn and do not submit', async ({ page }) => {
  let called = false
  await page.route('**/api/web/register', r => { called = true; r.fulfill({ json: { ok: true } }) })
  await page.goto('/register')
  await page.fill('input[name="email"]', 'a@b.com')
  await page.fill('input[name="username"]', 'Zugzug')
  await page.fill('input[name="password"]', 'secret1')
  await page.fill('input[name="password-v"]', 'secret2')
  await page.getByRole('button', { name: 'Register' }).click()
  await expect(page.getByText('Passwords must match.')).toBeVisible()
  expect(called).toBe(false)
})

test('valid registration redirects to login with success alert', async ({ page }) => {
  await page.route('**/api/web/register', r => r.fulfill({ json: { ok: true } }))
  await page.goto('/register')
  await page.fill('input[name="email"]', 'a@b.com')
  await page.fill('input[name="username"]', 'Zugzug')
  await page.fill('input[name="password"]', 'secret1')
  await page.fill('input[name="password-v"]', 'secret1')
  await page.getByRole('button', { name: 'Register' }).click()
  await expect(page).toHaveURL(/\/login/)
  await expect(page.getByText('Congratulations !')).toBeVisible()
})
```

- [ ] **Step 3: Run E2E** — `npm run test:e2e -- register.spec.ts` — Expected: PASS (2 tests).

- [ ] **Step 4: Commit**

```bash
git add WebTinyTank/pages/register.vue WebTinyTank/tests/e2e/register.spec.ts
git commit -m "feat(web): register page with validation parity"
```

---

## Task 14: Activation page (`/active/:id`)

**Legacy reference:** router `activation` — calls `active_account` with `{_idUser: id}`; on `res==1` success alert, else danger; then redirect `/profile`.

**Files:**
- Create: `pages/active/[id].vue`
- Test: `tests/e2e/active.spec.ts`

- [ ] **Step 1: Implement `pages/active/[id].vue`**

```vue
<script setup lang="ts">
const route = useRoute()
const api = useMasterApi()
const { notify } = useAlerts()
onMounted(async () => {
  try {
    await api.active(route.params.id as string)
    notify('Your account has been activated.', 'Well Done,', 'success')
  } catch {
    notify('Please try again.', 'Error, ', 'danger')
  } finally {
    await navigateTo('/profile')
  }
})
</script>
<template><div class="container text-center"><h2 class="page-header">Activating…</h2></div></template>
```

- [ ] **Step 2: Write `tests/e2e/active.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('activation calls active then redirects to profile', async ({ page }) => {
  await page.route('**/api/web/active', r => r.fulfill({ json: { ok: true } }))
  await page.route('**/api/web/profile**', r => r.fulfill({ status: 400, json: { statusCode: 400, message: 'User not found.' } }))
  await page.goto('/active/7')
  await expect(page).toHaveURL(/\/profile/)
  await expect(page.getByText('Your account has been activated.')).toBeVisible()
})
```

- [ ] **Step 3: Run E2E** — `npm run test:e2e -- active.spec.ts` — Expected: PASS.

- [ ] **Step 4: Commit**

```bash
git add WebTinyTank/pages/active WebTinyTank/tests/e2e/active.spec.ts
git commit -m "feat(web): account activation page"
```

---

## Task 15: About + Download pages

**Legacy reference:** `about.html` (logo, team section `#team` with 4 users `{name, picture}` from `about.js`, donate `#donate` w/ paypal, `#content`/`#development` empty sections, `#legal`). `download.html` (full layout, h1, jumbotron, two columns: download.png → `/downloads/TinyTank.zip`, signup.png → register).

**Files:**
- Create: `pages/about.vue`, `pages/download.vue`
- Test: `tests/e2e/static-pages.spec.ts`

- [ ] **Step 1: Implement `pages/about.vue`**

```vue
<script setup lang="ts">
useHead({ title: 'About — TinyTank' })
const users = [
  { name: 'Guillaume Lefrant', picture: '/img/team/ban.jpg' },
  { name: 'Kévin Andres', picture: '/img/team/fry.jpg' },
  { name: 'Styve Simonneau', picture: '/img/team/thugswiti.jpg' },
  { name: 'Alexandre Quintin', picture: '/img/team/hippolance.png' }
]
</script>
<template>
  <div class="container-fluid" id="aboutTinyTank">
    <h1 class="text-center page-header">About TinyTank</h1>
    <img src="/img/tinytank.png" class="img-responsive" alt="Logo" style="height:150px;margin:40px auto 0 auto" />
    <section id="team"><div class="container text-center">
      <h2 class="page-header col-md-6 col-md-offset-3">Our Team</h2>
      <table class="list-users"><tr><td v-for="u in users" :key="u.name">
        <div><h4>{{ u.name }}</h4><div class="circular"><img :src="u.picture" :alt="u.name" /></div></div>
      </td></tr></table>
    </div></section>
    <section id="donate"><div class="container text-center">
      <h2 class="page-header col-md-6 col-md-offset-3">Help us through PayPal</h2>
      <div class="row">
        <div class="col-md-9"><p>Building and maintaining a free online game isn't free for us ! We have to pay server, domain name, ...<br />Furthermore, we would like to give you a great experience by adding news contents or upgrade features time to time.<br />By donating, <strong>YOU will allow us</strong> to keep the game online and motivate us to bring you some new awesome stuff !<br />You have the power to keep this game alive, while you enjoy it.</p><p class="lead">But don't blast your Credit Card too much and blast foes on Tiny Tank !</p></div>
        <div class="col-md-3"><PaypalButton /></div>
      </div>
    </div></section>
    <section id="content"><div class="container text-center" /></section>
    <section id="development"><div class="container text-center" /></section>
    <section id="legal"><div class="container text-right"><p>We don't own everything on this project, all rights reserved for their respective owners. <br />Project is still building, you can follow us on <a href="//github.com/LeNiglo/TinyTank" target="_blank">GitHub</a>.</p></div></section>
  </div>
</template>
```

- [ ] **Step 2: Implement `pages/download.vue`** (full layout)

```vue
<script setup lang="ts">
definePageMeta({ layout: 'full' })
useHead({ title: 'Download — TinyTank' })
</script>
<template>
  <div>
    <h1 class="text-center page-header">Download TinyTank Now !</h1>
    <div class="container text-center">
      <div class="jumbotron text-left"><h1>TinyTank</h1><p>Compete with other people and enjoy your moment !</p></div>
      <div class="col-md-6"><a href="/downloads/TinyTank.zip" target="_blank"><img src="/img/download.png" alt="download" class="img-responsive" /></a></div>
      <div class="col-md-6"><NuxtLink to="/register"><img src="/img/signup.png" alt="register" class="img-responsive" /></NuxtLink></div>
    </div>
  </div>
</template>
```

- [ ] **Step 3: Write `tests/e2e/static-pages.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('about shows team and donate', async ({ page }) => {
  await page.goto('/about')
  await expect(page.getByRole('heading', { name: 'About TinyTank' })).toBeVisible()
  await expect(page.getByText('Guillaume Lefrant')).toBeVisible()
  await expect(page.locator('#donate')).toBeVisible()
})

test('download links to the game zip and register', async ({ page }) => {
  await page.goto('/download')
  await expect(page.locator('a[href="/downloads/TinyTank.zip"]')).toBeVisible()
  await expect(page.locator('img[src="/img/banner.png"]')).toBeVisible()   // full layout
})
```

- [ ] **Step 4: Run E2E** — `npm run test:e2e -- static-pages.spec.ts` — Expected: PASS (2 tests).

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/pages/about.vue WebTinyTank/pages/download.vue WebTinyTank/tests/e2e/static-pages.spec.ts
git commit -m "feat(web): about and download pages"
```

---

## Task 16: Forgot/Reset "coming soon" placeholders + 404

**Decision:** forgot/reset have no master endpoint (legacy used Meteor `accounts-ui`); render a "coming soon" placeholder. 404 = `notFound.html` ("Oooops ! Page not found." + Home button).

**Files:**
- Create: `pages/forgot-password.vue`, `pages/reset-password.vue`, `pages/[...slug].vue`
- Test: `tests/e2e/placeholders.spec.ts`

- [ ] **Step 1: Implement `pages/forgot-password.vue`**

```vue
<script setup lang="ts">
useHead({ title: 'Forgot password — TinyTank' })
</script>
<template>
  <div class="row text-center">
    <h3 class="page-header">Password lost ?</h3>
    <p class="lead">Password recovery is coming soon.</p>
    <p>In the meantime, please contact an admin to reset your password.</p>
    <div class="row text-center">
      <div class="col-md-6"><NuxtLink class="btn btn-primary" to="/" style="width:100%">Home</NuxtLink></div>
      <div class="col-md-6"><NuxtLink class="btn btn-info" to="/login" style="width:100%">Back to Login</NuxtLink></div>
    </div>
  </div>
</template>
```

- [ ] **Step 2: Implement `pages/reset-password.vue`** (same coming-soon treatment)

```vue
<script setup lang="ts">
useHead({ title: 'Reset password — TinyTank' })
</script>
<template>
  <div class="row text-center">
    <h3 class="page-header">Reset password</h3>
    <p class="lead">Password reset is coming soon.</p>
    <NuxtLink class="btn btn-primary" to="/login">Back to Login</NuxtLink>
  </div>
</template>
```

- [ ] **Step 3: Implement `pages/[...slug].vue`** (404)

```vue
<script setup lang="ts">
useHead({ title: 'Not found — TinyTank' })
</script>
<template>
  <div>
    <h1>Oooops ! Page not found.</h1>
    <br /><br />
    <div class="text-center"><NuxtLink class="btn btn-default" to="/">Home Page</NuxtLink></div>
  </div>
</template>
```

- [ ] **Step 4: Write `tests/e2e/placeholders.spec.ts`**

```ts
import { test, expect } from '@playwright/test'

test('forgot-password shows coming soon', async ({ page }) => {
  await page.goto('/forgot-password')
  await expect(page.getByText('Password recovery is coming soon.')).toBeVisible()
})
test('reset-password shows coming soon', async ({ page }) => {
  await page.goto('/reset-password')
  await expect(page.getByText('Password reset is coming soon.')).toBeVisible()
})
test('unknown route shows 404', async ({ page }) => {
  await page.goto('/this-does-not-exist')
  await expect(page.getByText('Oooops ! Page not found.')).toBeVisible()
})
```

- [ ] **Step 5: Run E2E** — `npm run test:e2e -- placeholders.spec.ts` — Expected: PASS (3 tests).

- [ ] **Step 6: Commit**

```bash
git add WebTinyTank/pages/forgot-password.vue WebTinyTank/pages/reset-password.vue "WebTinyTank/pages/[...slug].vue" WebTinyTank/tests/e2e/placeholders.spec.ts
git commit -m "feat(web): forgot/reset placeholders and 404 page"
```

---

## Task 17: Sentry (env-gated, no-op without DSN)

**Files:**
- Modify: `WebTinyTank/nuxt.config.ts`, `WebTinyTank/package.json`
- Create: `WebTinyTank/sentry.client.config.ts`, `WebTinyTank/sentry.server.config.ts`

**Interfaces:**
- Produces: Sentry init on client + server **only when `SENTRY_DSN` is set**; otherwise a no-op (no errors, no requests).

- [ ] **Step 1: Add `@sentry/nuxt` module to `nuxt.config.ts`**

Add `'@sentry/nuxt/module'` to `modules` and a `sentry` block keyed off the public DSN (the module is inert when DSN is empty):
```ts
modules: ['@nuxtjs/tailwindcss', '@sentry/nuxt/module'],
sentry: { autoInjectServerSentry: 'top-level' },
```

- [ ] **Step 2: Create `sentry.client.config.ts`**

```ts
import * as Sentry from '@sentry/nuxt'
const dsn = useRuntimeConfig().public.sentryDsn
if (dsn) Sentry.init({ dsn })
```

- [ ] **Step 3: Create `sentry.server.config.ts`**

```ts
import * as Sentry from '@sentry/nuxt'
if (process.env.SENTRY_DSN) Sentry.init({ dsn: process.env.SENTRY_DSN })
```

- [ ] **Step 4: Verify the app still builds and tests pass with no DSN**

Run:
```bash
unset SENTRY_DSN
npm run build
npm run test:e2e -- smoke.spec.ts
```
Expected: build succeeds; smoke test PASS; no Sentry network calls (DSN unset → init skipped).

- [ ] **Step 5: Commit**

```bash
git add WebTinyTank/nuxt.config.ts WebTinyTank/package.json WebTinyTank/sentry.client.config.ts WebTinyTank/sentry.server.config.ts
git commit -m "feat(web): wire env-gated Sentry (no-op without DSN)"
```

---

## Task 18: Dockerfile + build.sh deploy rewrite

**Files:**
- Create: `WebTinyTank/Dockerfile`, `WebTinyTank/.dockerignore`
- Modify: `build.sh` (root) — the `web()` function

**Interfaces:**
- Produces: a container serving `.output/`; a `web()` deploy that builds with npm and runs the Nitro server under the `tinytankweb` systemd service.

- [ ] **Step 1: Write `WebTinyTank/Dockerfile`** (multi-stage, mirrors DataTinyTank pattern)

```dockerfile
# ---- build ----
FROM node:22-slim AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---- runtime ----
FROM node:22-slim AS runtime
WORKDIR /app
ENV NODE_ENV=production PORT=3000
COPY --from=build /app/.output ./.output
EXPOSE 3000
CMD ["node", ".output/server/index.mjs"]
```

`WebTinyTank/.dockerignore`:
```
node_modules
.nuxt
.output
tests
test-results
playwright-report
.git
```

- [ ] **Step 2: Rewrite the `web()` function in root `build.sh`**

Replace the existing Meteor `web()` (the `meteor update`/`meteor build`/tar block) with:
```bash
function web {
  echo "Building Web"
  cd WebTinyTank
  npm ci
  npm run build
  sudo service tinytankweb stop
  sudo rm -rf /opt/tinytank/web/.output
  sudo cp -r .output /opt/tinytank/web/.output
  sudo cp -r public /opt/tinytank/web/public
  sudo chown tinytank -R /opt/tinytank
  sudo service tinytankweb start
  cd ..
}
```
(Leave the `data()` function and the rest of `build.sh` untouched. The `tinytankweb` systemd unit must run `node /opt/tinytank/web/.output/server/index.mjs` with `API_URL`/`API_AUTH`/`SENTRY_DSN`/`PORT` in its environment — note this in the commit body; the unit file lives on the server, not in the repo.)

- [ ] **Step 3: Validate the Docker build**

Run:
```bash
cd WebTinyTank && docker build -t tinytank-web . && cd ..
```
Expected: image builds; `docker run -e API_URL=... -e API_AUTH=... -p 3000:3000 tinytank-web` serves the app on :3000 (manual smoke optional).

- [ ] **Step 4: Commit**

```bash
git add WebTinyTank/Dockerfile WebTinyTank/.dockerignore build.sh
git commit -m "chore(web): add Dockerfile and rewrite build.sh web() for Nuxt"
```

---

## Task 19: Cleanup, full test sweep, rewrite WebTinyTank/CLAUDE.md

**Files:**
- Delete: any leftover Meteor artifacts in `WebTinyTank/` (confirm `.meteor`, `client/`, `lib/`, `server/methods.js` legacy, `private/`, `WebTinyTank.iml`, `public/css/bootstrap.cosmo.css` + `public/js/bootstrap.min.js` if unused) — keep `public/img`, `public/fonts`, `public/downloads`, `public/favicon.ico`.
- Modify: `WebTinyTank/CLAUDE.md` (rewrite for the Nuxt app)

**Interfaces:** none (finalization).

- [ ] **Step 1: Confirm no legacy Meteor files remain**

Run:
```bash
cd WebTinyTank && ls -la && test ! -d .meteor && test ! -d lib && echo "clean" || echo "LEFTOVERS"
```
Expected: `clean`. (Task 1 removed them; this verifies. Remove `WebTinyTank.iml` if present.) Decide on `public/css`/`public/js`: if no page references `bootstrap.cosmo.css`/`bootstrap.min.js` (we use Tailwind), remove them; keep `public/css/style.css` only if any ported class depends on it.

- [ ] **Step 2: Run the FULL test suite**

Run:
```bash
npm run test:unit
npm run test:e2e
```
Expected: all Vitest specs PASS; all Playwright specs PASS (smoke, shell, home, ladder, servers, profile, tank, login, register, active, static-pages, placeholders).

- [ ] **Step 3: Rewrite `WebTinyTank/CLAUDE.md`** for the Nuxt app

Replace the Meteor description with: Nuxt 3 + Vue 3 + TS; Nitro `server/api/web/*` BFF (8 routes) holding `API_AUTH`, normalizing the `{res,err}` envelope and whitelisting user fields; `runtimeConfig` env vars (`API_URL`, `API_AUTH`, `SENTRY_DSN`, `PORT`); composables (`useAuth`, `usePolling`, `useAlerts`, `useMasterApi`); routes table (the ~13 pages); localStorage-token auth (client-resolved, no cookie); polling intervals (ladder/servers 60s, infos 120s); testing (`npm run test:unit` Vitest, `npm run test:e2e` Playwright); build/deploy (`npm run build` → `.output/`, systemd `tinytankweb`, Dockerfile); shared `public/downloads/` contract.

- [ ] **Step 4: Commit**

```bash
git add WebTinyTank
git commit -m "chore(web): remove legacy Meteor files; rewrite WebTinyTank CLAUDE.md"
```

- [ ] **Step 5: Final verification before handoff**

Run:
```bash
npm run build && npm run test:unit && npm run test:e2e
```
Expected: build + all tests green. The rebuild is complete on branch `worktree-feat-web-rebuild`, ready to merge to `dev`.

---

## Self-Review notes (author)

- **Spec coverage:** framework (T1), BFF + envelope + whitelist (T2/T3), types (T3), composables (T4/T5), shell/layouts (T6), all ~13 pages (T7–T16: home, ladder, servers, profile, tank, login, register, active, about, download, forgot, reset, 404), Sentry (T17), deploy + Dockerfile + build.sh (T18), shared-contract preservation (Global Constraints + T1 asset carry), CLAUDE.md + cleanup (T19). Every spec section maps to a task.
- **Field-whitelist security** (login/profile drop `password`) is enforced in T2 (unit-tested) and applied in T3.
- **Naming consistency:** `useMasterApi` methods (`getInfos/getServers/getLadder/getProfile/getTanks/register/login/active`) match the routes and the page consumers throughout. `whitelistLogin`/`whitelistProfile`/`unwrapEnvelope`/`callMaster`/`toHttpError` names are consistent across T2 and T3.
- **Per-page Playwright** mocks the Nitro routes at the browser boundary (`page.route('**/api/web/**')`) — deterministic, no live master — satisfying the owner's "every page programmatically verifiable" requirement.
