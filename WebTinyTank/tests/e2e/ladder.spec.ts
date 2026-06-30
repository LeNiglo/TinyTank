import { test, expect } from '@playwright/test'

const LADDER = [
  { rank: 1, username: 'LeNiglo', _id: 'a', gamesPlayed: 1337, killCount: 3214, accuracy: 85.2 },
  { rank: 2, username: 'Switi', _id: 'b', gamesPlayed: 1302, killCount: 729, accuracy: 87.8 },
  { rank: 3, username: 'DraymZz', _id: 'c', gamesPlayed: 668, killCount: 520, accuracy: 83.0 }
]

test('ladder renders rows and top-1 gold star', async ({ page }) => {
  await page.route('**/api/web/ladder', (r) => r.fulfill({ json: LADDER }))
  await page.goto('/ladder')
  await expect(page.getByRole('link', { name: 'LeNiglo' })).toBeVisible()
  await expect(page.locator('#ladder tbody tr')).toHaveCount(3)
  await expect(page.locator('#ladder tbody tr').first().locator('.medal .glyphicon-star')).toBeVisible()
  await expect(page.getByText('85.2 %')).toBeVisible()
})
