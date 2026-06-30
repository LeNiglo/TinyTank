import * as Sentry from '@sentry/browser'

// Client-side error reporting. Completely inert unless SENTRY_DSN is configured
// (exposed via runtimeConfig.public.sentryDsn) — no DSN, no init, no requests.
export default defineNuxtPlugin((nuxtApp) => {
  const dsn = useRuntimeConfig().public.sentryDsn
  if (!dsn) return

  Sentry.init({ dsn })

  nuxtApp.hook('vue:error', (err) => {
    Sentry.captureException(err)
  })
})
