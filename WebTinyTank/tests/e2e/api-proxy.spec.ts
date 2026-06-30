import { test, expect } from '@playwright/test'

// Exercises the REAL Nitro BFF routes (no browser-boundary mocking) against the
// stub master server. This is the only test of the actual proxy + envelope + whitelist.

test('login route whitelists fields (drops password/email)', async ({ request }) => {
  const res = await request.post('/api/web/login', { data: { login: 'Bob', password: 'x' } })
  expect(res.ok()).toBeTruthy()
  expect(await res.json()).toEqual({ _id: '1', username: 'Bob', token: 'TOK' })
})

test('profile route surfaces master err as an HTTP error', async ({ request }) => {
  const res = await request.get('/api/web/profile?id=nobody')
  expect(res.status()).toBe(400)
})
