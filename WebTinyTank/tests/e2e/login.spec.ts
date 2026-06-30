import { test, expect } from '@playwright/test'
import { gotoHydrated } from './helpers'

const PROFILE_JSON = {
  _id: '7', username: 'Zug', from: '', createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
  stats: { gamesPlayed: 0, kills: 0, deaths: 0, score: 0, shotsFired: 0, shotsHit: 0, killsPG: 0, deathsPG: 0, scorePG: 0, shotsFiredPG: 0, shotsHitPG: 0 }
}

test('successful login stores session and redirects to profile', async ({ page }) => {
  await page.route('**/api/web/login', (r) => r.fulfill({ json: { _id: '7', username: 'Zug', token: 'TOK' } }))
  await page.route('**/api/web/profile**', (r) => r.fulfill({ json: PROFILE_JSON }))
  await gotoHydrated(page, '/login')
  await page.fill('input[name="email"]', 'Zug')
  await page.fill('input[name="password"]', 'secret')
  await page.getByRole('button', { name: 'Login to your Account' }).click()
  await expect(page).toHaveURL(/\/profile/)
  expect(await page.evaluate(() => localStorage.getItem('authToken'))).toBe('TOK')
  await expect(page.getByText('Welcome Back !')).toBeVisible()
})

test('failed login shows danger alert', async ({ page }) => {
  await page.route('**/api/web/login', (r) =>
    r.fulfill({ status: 400, json: { statusCode: 400, message: 'Account not active.' } })
  )
  await gotoHydrated(page, '/login')
  await page.fill('input[name="email"]', 'Zug')
  await page.fill('input[name="password"]', 'bad')
  await page.getByRole('button', { name: 'Login to your Account' }).click()
  await expect(page.getByText('Account not active.')).toBeVisible()
})
