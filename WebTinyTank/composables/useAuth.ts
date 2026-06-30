/**
 * Manual auth, faithful to the legacy app: a localStorage token echo, no cookie/session.
 * Reactive across the app via useState; token-dependent UI must call hydrate() on the
 * client (e.g. in onMounted) since localStorage is unavailable during SSR.
 */
export function useAuth() {
  const isConnected = useState('auth:isConnected', () => false)
  const username = useState<string | null>('auth:username', () => null)
  const authId = useState<string | null>('auth:id', () => null)

  function hydrate() {
    if (import.meta.client) {
      const tok = localStorage.getItem('authToken')
      const id = localStorage.getItem('authID')
      isConnected.value = !!(tok && id)
      username.value = localStorage.getItem('authUsername')
      authId.value = id
    }
  }

  function setSession(r: { _id: string; username: string; token: string }) {
    localStorage.setItem('authToken', r.token)
    localStorage.setItem('authID', r._id)
    localStorage.setItem('authUsername', r.username)
    isConnected.value = true
    username.value = r.username
    authId.value = r._id
  }

  function logout() {
    localStorage.clear()
    isConnected.value = false
    username.value = null
    authId.value = null
  }

  return { isConnected, username, authId, hydrate, setSession, logout }
}
