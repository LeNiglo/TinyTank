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
  await expect(page.locator('img[src="/img/banner.png"]')).toBeVisible() // full layout
})
