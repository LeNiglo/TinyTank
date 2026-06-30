import type { Infos, Server, LadderRow, Profile, Tank, LoginResult } from '~/types/master'

/** Typed wrappers over the same-origin Nitro BFF routes (`/api/web/*`). */
export function useMasterApi() {
  return {
    getInfos: () => $fetch<Infos>('/api/web/infos'),
    getServers: () => $fetch<Server[]>('/api/web/servers'),
    getLadder: () => $fetch<LadderRow[]>('/api/web/ladder'),
    getProfile: (id: string) => $fetch<Profile>('/api/web/profile', { query: { id } }),
    getTanks: () => $fetch<Tank[]>('/api/web/tanks'),
    register: (data: { username: string; email: string; password: string; from: string }) =>
      $fetch<{ ok: true }>('/api/web/register', { method: 'POST', body: data }),
    login: (data: { login: string; password: string }) =>
      $fetch<LoginResult>('/api/web/login', { method: 'POST', body: data }),
    active: (id: string) => $fetch<{ ok: true }>('/api/web/active', { method: 'POST', body: { id } })
  }
}
