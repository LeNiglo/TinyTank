import type { Infos } from '~/types/master'

export default defineEventHandler(async () => {
  try {
    return await callMaster<Infos>('GET', '/get_infos')
  } catch (e) {
    throw toHttpError(e)
  }
})
