import type { Server } from '~/types/master'

export default defineEventHandler(async () => {
  try {
    return await callMaster<Server[]>('GET', '/list_servers')
  } catch (e) {
    throw toHttpError(e)
  }
})
