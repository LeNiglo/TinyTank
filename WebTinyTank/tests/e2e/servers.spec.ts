import { test, expect } from '@playwright/test'

test('servers list renders rows with player count', async ({ page }) => {
  await page.route('**/api/web/servers', (r) =>
    r.fulfill({
      json: [
        {
          _id: 's1',
          name: 'EU-1',
          ip: '1.2.3.4',
          ports: { udp: 1, tcp: 2 },
          users: ['a', 'b'],
          map: 'desert',
          started_at: new Date().toISOString(),
          last_active: new Date().toISOString()
        }
      ]
    })
  )
  await page.goto('/servers-list')
  await expect(page.getByText('EU-1')).toBeVisible()
  await expect(page.getByText('desert')).toBeVisible()
  await expect(page.locator('table tbody tr td').last()).toHaveText('2')
})
