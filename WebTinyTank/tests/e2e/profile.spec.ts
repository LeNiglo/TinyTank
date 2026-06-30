import { test, expect } from '@playwright/test'

const PROFILE = {
  _id: '7',
  username: 'Zug',
  from: 'France',
  createdAt: new Date(Date.now() - 86400000).toISOString(),
  updatedAt: new Date().toISOString(),
  stats: {
    gamesPlayed: 2, kills: 10, deaths: 4, score: 50, shotsFired: 100, shotsHit: 73,
    killsPG: 5, deathsPG: 2, scorePG: 25, shotsFiredPG: 50, shotsHitPG: 36.5
  }
}

test('profile by id renders stats and accuracy', async ({ page }) => {
  await page.route('**/api/web/profile**', (r) => r.fulfill({ json: PROFILE }))
  await page.goto('/profile/7')
  await expect(page.getByRole('heading', { name: 'Zug' })).toBeVisible()
  await expect(page.getByText('73.00 %')).toBeVisible() // 73/100*100
})

test('profile without id and no auth redirects to login', async ({ page }) => {
  await page.goto('/profile')
  await expect(page).toHaveURL(/\/login/)
})

test('unknown profile shows Not Found + search', async ({ page }) => {
  await page.route('**/api/web/profile**', (r) =>
    r.fulfill({ status: 400, json: { statusCode: 400, message: 'User not found.' } })
  )
  await page.goto('/profile/nobody')
  await expect(page.getByText('User Not Found.')).toBeVisible()
  await expect(page.locator('input[name="search"]').first()).toBeVisible()
})
