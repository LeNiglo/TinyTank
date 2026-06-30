/** Pure interval helper: runs fn immediately, then every `ms`. Returns a stop fn. */
export function startPolling(fn: () => void | Promise<void>, ms: number) {
  fn()
  const id = setInterval(fn, ms)
  return () => clearInterval(id)
}

/** Component-lifecycle polling: starts on mount, clears on unmount. */
export function usePolling(fn: () => void | Promise<void>, ms: number) {
  let stop = () => {}
  onMounted(() => {
    stop = startPolling(fn, ms)
  })
  onUnmounted(() => stop())
  return { stop: () => stop() }
}
