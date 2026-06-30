import type { Config } from 'tailwindcss'

export default <Partial<Config>>{
  // Faithful port reuses the original Bootstrap 3 (Cosmo) stylesheet. Disable
  // Tailwind's preflight so it does not reset Bootstrap's base styles. Tailwind
  // utilities remain available for any net-new UI.
  corePlugins: { preflight: false },
  content: [
    './components/**/*.{vue,ts}',
    './layouts/**/*.vue',
    './pages/**/*.vue',
    './app.vue'
  ]
}
