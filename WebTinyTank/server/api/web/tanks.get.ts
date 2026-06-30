import type { Tank } from '~/types/master'

export default defineEventHandler(async () => {
  try {
    return await callMaster<Tank[]>('GET', '/get_tank_list')
  } catch (e) {
    throw toHttpError(e)
  }
})
