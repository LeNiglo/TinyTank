import type { Profile } from '~/types/master'

export default defineEventHandler(async (event) => {
  const { id } = getQuery(event)
  if (!id) throw createError({ statusCode: 400, statusMessage: 'Missing id' })
  try {
    const user = await callMaster<any>('GET', '/user_profile', { query: { _idUser: id } })
    return whitelistProfile(user) as Profile
  } catch (e) {
    throw toHttpError(e)
  }
})
