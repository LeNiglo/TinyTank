import * as Sentry from '@sentry/node'

// Server-side (Nitro) error reporting. Inert unless SENTRY_DSN is set.
export default defineNitroPlugin((nitroApp) => {
  const dsn = process.env.SENTRY_DSN
  if (!dsn) return

  Sentry.init({ dsn })

  nitroApp.hooks.hook('error', (error) => {
    Sentry.captureException(error)
  })
})
