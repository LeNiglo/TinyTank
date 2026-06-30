import { defineVitestConfig } from '@nuxt/test-utils/config'

export default defineVitestConfig({
  test: {
    include: ['tests/unit/**/*.spec.ts'],
    // Default to the Nuxt environment so Nuxt auto-imports (useState, etc.) work
    // in composable tests. Pure-logic and e2e-setup tests opt out per-file with
    // a `// @vitest-environment node` docblock.
    environment: 'nuxt'
  }
})
