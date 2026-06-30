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
