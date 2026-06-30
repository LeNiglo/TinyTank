export interface Infos {
  last: { username: string; _id: string; createdAt: string } | null
  nb_users: number
}

export interface Server {
  _id: string
  name: string
  ip: string
  ports: { udp: number; tcp: number }
  users: string[]
  map: string
  started_at: string
  last_active: string
}

export interface LadderRow {
  rank: number
  username: string
  _id: string
  gamesPlayed: number
  killCount: number
  accuracy: number
}

export interface ProfileStats {
  gamesPlayed: number
  kills: number
  deaths: number
  score: number
  shotsFired: number
  shotsHit: number
  killsPG: number
  deathsPG: number
  scorePG: number
  shotsFiredPG: number
  shotsHitPG: number
}

export interface Profile {
  _id: string
  username: string
  from: string
  createdAt: string
  updatedAt: string
  stats: ProfileStats
}

export interface Tank {
  _id: string
  [k: string]: any
}

export interface LoginResult {
  _id: string
  username: string
  token: string
}
