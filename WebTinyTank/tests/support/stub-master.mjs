// Deterministic stand-in for the DataTinyTank master web API, used during E2E.
// Returns the raw `{ name, res, err }` envelope (including a password hash on login)
// so we can assert the BFF normalizes + whitelists correctly. Page tests mock at the
// browser boundary and never reach this; it only backs the api-proxy spec and any
// un-mocked call (kept deterministic instead of hitting unreachable tinytank.dev).
import { createServer } from 'node:http'

const PORT = Number(process.env.STUB_MASTER_PORT || 4599)

function send(res, body) {
  res.setHeader('content-type', 'application/json')
  res.end(JSON.stringify(body))
}

const server = createServer((req, res) => {
  const url = req.url || ''
  let raw = ''
  req.on('data', (c) => (raw += c))
  req.on('end', () => {
    if (url === '/' || url.startsWith('/health')) return send(res, { ok: true })
    if (url.startsWith('/login')) {
      return send(res, {
        name: 'login',
        res: { _id: '1', username: 'Bob', token: 'TOK', password: 'HASH', email: 'b@x.io' },
        err: null
      })
    }
    if (url.startsWith('/user_profile')) {
      return send(res, { name: 'user_profile', res: null, err: 'User not found.' })
    }
    if (url.startsWith('/get_infos')) {
      return send(res, { name: 'get_infos', res: { last: null, nb_users: 0 }, err: null })
    }
    if (url.startsWith('/list_servers')) return send(res, { name: 'list_servers', res: [], err: null })
    if (url.startsWith('/ladder')) return send(res, { name: 'ladder', res: [], err: null })
    if (url.startsWith('/get_tank_list')) return send(res, { name: 'get_tank_list', res: [], err: null })
    if (url.startsWith('/register')) return send(res, { name: 'register', res: true, err: null })
    if (url.startsWith('/active_account')) return send(res, { name: 'active_account', res: { ok: 1 }, err: null })
    res.statusCode = 404
    send(res, { name: 'unknown', res: false, err: 'unknown path' })
  })
})

server.listen(PORT, '127.0.0.1', () => {
  // eslint-disable-next-line no-console
  console.log(`stub-master listening on http://127.0.0.1:${PORT}`)
})
