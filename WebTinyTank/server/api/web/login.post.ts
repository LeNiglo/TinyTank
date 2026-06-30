import type { LoginResult } from '~/types/master'

export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  try {
    const user = await callMaster<any>('POST', '/login', {
      body: { login: body.login, password: body.password }
    })
    return whitelistLogin(user) as LoginResult
  } catch (e) {
    throw toHttpError(e)
  }
})
