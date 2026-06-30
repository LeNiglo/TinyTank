import { describe, it, expect } from 'vitest'
import { unwrapEnvelope, whitelistLogin, whitelistProfile, MasterError } from '../../server/utils/master'

describe('unwrapEnvelope', () => {
  it('returns res on success', () => {
    expect(unwrapEnvelope({ name: 'x', res: { a: 1 }, err: null })).toEqual({ a: 1 })
  })
  it('throws MasterError with err when res is false', () => {
    expect(() => unwrapEnvelope({ name: 'login', res: false, err: 'Account not active.' }))
      .toThrowError('Account not active.')
  })
  it('throws when res is null and err present', () => {
    expect(() => unwrapEnvelope({ name: 'user_profile', res: null, err: 'User not found.' }))
      .toThrowError('User not found.')
  })
  it('allows falsy-but-valid res like empty array', () => {
    expect(unwrapEnvelope({ name: 'x', res: [], err: null })).toEqual([])
  })
})

describe('field whitelists', () => {
  const user = {
    _id: '1', username: 'Bob', password: 'HASH', token: 'TOK',
    from: 'France', createdAt: 'd', updatedAt: 'u', stats: { kills: 3 }
  }
  it('login exposes only _id, username, token', () => {
    expect(whitelistLogin(user)).toEqual({ _id: '1', username: 'Bob', token: 'TOK' })
  })
  it('profile excludes password and token', () => {
    const p = whitelistProfile(user)
    expect(p).toEqual({ _id: '1', username: 'Bob', from: 'France', createdAt: 'd', updatedAt: 'u', stats: { kills: 3 } })
    expect((p as any).password).toBeUndefined()
    expect((p as any).token).toBeUndefined()
  })
})

describe('MasterError', () => {
  it('carries a statusCode', () => {
    const e = new MasterError('nope', 404)
    expect(e.statusCode).toBe(404)
    expect(e.message).toBe('nope')
  })
})
