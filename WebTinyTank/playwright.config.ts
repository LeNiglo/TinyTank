import { defineConfig } from '@playwright/test'

const STUB_PORT = 4599

export default defineConfig({
  testDir: './tests/e2e',
  use: { baseURL: 'http://localhost:3000' },
  webServer: [
    {
      // Deterministic stub of the master web API.
      command: 'node tests/support/stub-master.mjs',
      port: STUB_PORT,
      reuseExistingServer: !process.env.CI
    },
    {
      // Nuxt dev server, with its BFF pointed at the stub master.
      command: 'npm run dev',
      url: 'http://localhost:3000',
      reuseExistingServer: !process.env.CI,
      timeout: 120_000,
      env: {
        API_URL: `http://127.0.0.1:${STUB_PORT}`,
        API_AUTH: 'user:pass'
      }
    }
  ]
})
