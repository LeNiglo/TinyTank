import type { LadderRow } from '~/types/master'

export default defineEventHandler(async () => {
  try {
    return await callMaster<LadderRow[]>('GET', '/ladder')
  } catch (e) {
    throw toHttpError(e)
  }
})
