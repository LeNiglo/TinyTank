import { test, expect } from '@playwright/test'

test('activation calls active, alerts success, and leaves the active page', async ({ page }) => {
  await page.route('**/api/web/active', (r) => r.fulfill({ json: { ok: true } }))
  await page.route('**/api/web/profile**', (r) =>
    r.fulfill({ status: 400, json: { statusCode: 400, message: 'User not found.' } })
  )
  await page.goto('/active/7')
  // Redirects to /profile; since a freshly-activated user is not logged in, /profile
  // bounces to /login. The success alert (shared state) survives the SPA navigation.
  await expect(page.getByText('Your account has been activated.')).toBeVisible()
  await expect(page).not.toHaveURL(/\/active/)
})
