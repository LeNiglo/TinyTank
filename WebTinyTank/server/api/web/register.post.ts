export default defineEventHandler(async (event) => {
  const body = await readBody(event)
  try {
    await callMaster<boolean>('POST', '/register', {
      body: {
        username: body.username,
        email: body.email,
        password: body.password,
        from: body.from,
        createdAt: new Date()
      }
    })
    return { ok: true }
  } catch (e) {
    throw toHttpError(e)
  }
})
