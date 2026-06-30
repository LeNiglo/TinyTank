import { test, expect } from '@playwright/test'
import { gotoHydrated } from './helpers'

test('mismatched passwords warn and do not submit', async ({ page }) => {
  let called = false
  await page.route('**/api/web/register', (r) => {
    called = true
    r.fulfill({ json: { ok: true } })
  })
  await gotoHydrated(page, '/register')
  await page.fill('input[name="email"]', 'a@b.com')
  await page.fill('input[name="username"]', 'Zugzug')
  await page.fill('input[name="password"]', 'secret1')
  await page.fill('input[name="password-v"]', 'secret2')
  await page.getByRole('button', { name: 'Register' }).click()
  await expect(page.getByText('Passwords must match.')).toBeVisible()
  expect(called).toBe(false)
})

test('valid registration redirects to login with success alert', async ({ page }) => {
  await page.route('**/api/web/register', (r) => r.fulfill({ json: { ok: true } }))
  await gotoHydrated(page, '/register')
  await page.fill('input[name="email"]', 'a@b.com')
  await page.fill('input[name="username"]', 'Zugzug')
  await page.fill('input[name="password"]', 'secret1')
  await page.fill('input[name="password-v"]', 'secret1')
  await page.getByRole('button', { name: 'Register' }).click()
  await expect(page).toHaveURL(/\/login/)
  await expect(page.getByText('Congratulations !')).toBeVisible()
})
