import { test, expect } from '@playwright/test'

test('home renders sections, banner, and polled infos', async ({ page }) => {
  await page.route('**/api/web/infos', (r) =>
    r.fulfill({
      json: { last: { username: 'Zug', _id: '7', createdAt: new Date().toISOString() }, nb_users: 42 }
    })
  )
  await page.goto('/')
  await expect(page.getByRole('heading', { name: 'Tiny Tank Official' })).toBeVisible()
  await expect(page.getByText('Fun Comes First')).toBeVisible()
  await expect(page.locator('img[src="/img/banner.png"]')).toBeVisible() // full layout
  await expect(page.getByText('42 registered users.')).toBeVisible()
  await expect(page.getByRole('link', { name: 'Zug' })).toBeVisible()
})

test('banner message rotates', async ({ page }) => {
  await page.route('**/api/web/infos', (r) => r.fulfill({ json: { last: null, nb_users: 0 } }))
  await page.goto('/')
  await expect(page.locator('#tinyHomeMessage')).toHaveText('Have Fun')
  await expect(page.locator('#tinyHomeMessage')).toHaveText('Get Blasted', { timeout: 4000 })
})
