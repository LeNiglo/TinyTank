import { test, expect } from '@playwright/test'

test('app boots and renders a page', async ({ page }) => {
  await page.goto('/')
  await expect(page).toHaveTitle(/TinyTank/)
})
