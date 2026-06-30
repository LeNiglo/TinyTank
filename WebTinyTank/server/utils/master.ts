import { Buffer } from 'node:buffer'

export class MasterError extends Error {
  statusCode: number
  constructor(message: string, statusCode = 400) {
    super(message || 'Master server error')
    this.name = 'MasterError'
    this.statusCode = statusCode
  }
}

export interface Envelope<T> {
  name: string
  res: T | false | null
  err: string | null
}

/**
 * Normalize the master server's `{ name, res, err }` envelope.
 * The master signals failure as HTTP 200 with `res: false` (or `res: null` + `err`),
 * so we translate those into a thrown MasterError. On success we return `res` —
 * including falsy-but-valid values like `[]`, `0` or `''`.
 */
export function unwrapEnvelope<T>(payload: Envelope<T>): T {
  if (payload.res === false || (payload.res == null && payload.err)) {
    throw new MasterError(payload.err || 'Request failed')
  }
  return payload.res as T
}

export function whitelistLogin(u: any) {
  return { _id: u._id, username: u.username, token: u.token }
}

export function whitelistProfile(u: any) {
  return {
    _id: u._id,
    username: u.username,
    from: u.from,
    createdAt: u.createdAt,
    updatedAt: u.updatedAt,
    stats: u.stats
  }
}

/**
 * Call the master server's web API with HTTP Basic auth, then unwrap the envelope.
 * Credentials come from server-only runtimeConfig and never reach the browser.
 */
export async function callMaster<T>(
  method: 'GET' | 'POST',
  path: string,
  opts: { query?: Record<string, any>; body?: Record<string, any> } = {}
): Promise<T> {
  const cfg = useRuntimeConfig()
  const auth = 'Basic ' + Buffer.from(cfg.apiAuth).toString('base64')
  const payload = await $fetch<Envelope<T>>(cfg.apiUrl + path, {
    method,
    headers: { Authorization: auth },
    query: opts.query,
    body: opts.body
  })
  return unwrapEnvelope<T>(payload)
}

/** Map a thrown MasterError (or any error) to an H3 HTTP error for the BFF routes. */
export function toHttpError(e: unknown) {
  if (e instanceof MasterError) {
    return createError({ statusCode: e.statusCode, statusMessage: e.message })
  }
  return createError({ statusCode: 502, statusMessage: 'Master server unreachable' })
}
