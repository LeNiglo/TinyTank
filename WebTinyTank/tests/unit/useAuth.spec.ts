import { describe, it, expect, beforeEach } from 'vitest'
import { useAuth } from '../../composables/useAuth'

beforeEach(() => localStorage.clear())

describe('useAuth', () => {
  it('starts disconnected after hydrate with empty storage', () => {
    const a = useAuth()
    a.hydrate()
    expect(a.isConnected.value).toBe(false)
  })
  it('setSession stores token/id/username and connects', () => {
    const a = useAuth()
    a.setSession({ _id: '7', username: 'Zug', token: 'TOK' })
    expect(localStorage.getItem('authToken')).toBe('TOK')
    expect(localStorage.getItem('authID')).toBe('7')
    expect(localStorage.getItem('authUsername')).toBe('Zug')
    expect(a.isConnected.value).toBe(true)
    expect(a.username.value).toBe('Zug')
  })
  it('logout clears storage and disconnects', () => {
    const a = useAuth()
    a.setSession({ _id: '7', username: 'Zug', token: 'TOK' })
    a.logout()
    expect(localStorage.getItem('authToken')).toBeNull()
    expect(a.isConnected.value).toBe(false)
  })
})
