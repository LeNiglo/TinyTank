// @vitest-environment node
import { describe, it, expect, vi } from 'vitest'
import { startPolling } from '../../composables/usePolling'

describe('startPolling', () => {
  it('calls fn immediately and on each interval; stop() clears it', () => {
    vi.useFakeTimers()
    const fn = vi.fn()
    const stop = startPolling(fn, 1000)
    expect(fn).toHaveBeenCalledTimes(1) // immediate
    vi.advanceTimersByTime(2500)
    expect(fn).toHaveBeenCalledTimes(3) // +2 ticks
    stop()
    vi.advanceTimersByTime(5000)
    expect(fn).toHaveBeenCalledTimes(3) // no more
    vi.useRealTimers()
  })
})
