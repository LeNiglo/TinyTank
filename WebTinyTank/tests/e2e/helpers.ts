import type { Page } from '@playwright/test'

/**
 * Navigate and wait for Nuxt client hydration to complete before interacting.
 * In dev mode, 'networkidle' settles after the module graph loads, by which point
 * Vue has hydrated and v-model / event handlers are live. Use this before any test
 * that types into inputs or relies on client-side reactivity.
 */
export async function gotoHydrated(page: Page, path: string) {
  await page.goto(path, { waitUntil: 'networkidle' })
}
