import { test, expect } from '@playwright/test'
import { gotoHydrated } from './helpers'

test('header shows Login when logged out, nav links present', async ({ page }) => {
  await page.goto('/')
  const nav = page.locator('nav')
  await expect(nav.getByRole('link', { name: 'Login/Register' })).toBeVisible()
  await expect(nav.getByRole('link', { name: 'Ladder' })).toBeVisible()
  await expect(nav.getByRole('link', { name: 'Servers List' })).toBeVisible()
  await expect(nav.getByRole('link', { name: 'Download' })).toBeVisible()
})

test('search box navigates to profile', async ({ page }) => {
  await gotoHydrated(page, '/')
  await page.fill('input[name="search"]', 'Bob')
  await page.locator('nav').getByRole('button', { name: 'Search' }).click()
  await expect(page).toHaveURL(/\/profile\/Bob/)
})

test('logged-in header shows username + logout', async ({ page }) => {
  await page.addInitScript(() => {
    localStorage.setItem('authToken', 'T')
    localStorage.setItem('authID', '7')
    localStorage.setItem('authUsername', 'Zug')
  })
  await page.goto('/')
  await expect(page.locator('nav').getByRole('link', { name: 'Zug' })).toBeVisible()
  await expect(page.locator('#logout')).toBeVisible()
})
