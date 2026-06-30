export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  try {
    await callMaster<any>('POST', '/active_account', { body: { _idUser: body.id } })
    return { ok: true }
  } catch (e) {
    throw toHttpError(e)
  }
})
