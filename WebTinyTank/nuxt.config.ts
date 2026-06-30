// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-01-01',
  devtools: { enabled: true },
  modules: ['@nuxtjs/tailwindcss'],
  css: ['~/assets/css/main.css'],
  runtimeConfig: {
    // server-only — NEVER exposed to the client
    apiUrl: process.env.API_URL || 'http://tinytank.dev/api/web',
    apiAuth:
      process.env.API_AUTH ||
      'T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv:lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo',
    public: {
      sentryDsn: process.env.SENTRY_DSN || ''
    }
  },
  app: {
    head: {
      title: 'TinyTank',
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
        // Faithful port: reuse the original Cosmo (dark-red) Bootstrap 3 theme +
        // custom styles, served as static assets. The ported markup is all
        // Bootstrap-3 classes, so this gives a pixel-faithful look with no rewrite.
        { rel: 'stylesheet', href: '/css/bootstrap.cosmo.css' },
        { rel: 'stylesheet', href: '/css/style.css' }
      ]
    }
  }
})
