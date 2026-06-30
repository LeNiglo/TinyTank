import { test, expect } from '@playwright/test'

test('tank list renders from catalog', async ({ page }) => {
  await page.route('**/api/web/tanks', (r) =>
    r.fulfill({ json: [{ _id: 't1', name: 'Sherman' }, { _id: 't2', name: 'Tiger' }] })
  )
  await page.goto('/tank')
  await expect(page.getByRole('link', { name: 'Sherman' })).toBeVisible()
  await expect(page.getByRole('link', { name: 'Tiger' })).toBeVisible()
})

test('tank detail shows selected tank', async ({ page }) => {
  await page.route('**/api/web/tanks', (r) =>
    r.fulfill({ json: [{ _id: 't1', name: 'Sherman', hp: 100 }] })
  )
  await page.goto('/tank/t1')
  await expect(page.getByRole('heading', { name: 'Sherman' })).toBeVisible()
  await expect(page.getByText('hp')).toBeVisible()
})
