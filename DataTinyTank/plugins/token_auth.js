var jwt = require('jwt-simple');

// Exported as a factory so the middleware can reach `app` (and its jwtTokenSecret).
// Best-effort: a missing/invalid/expired token never blocks the request — HTTP Basic
// auth (see config.js) is the real gate. We only attach req.user when a token decodes.
module.exports = function (app) {

  return async function (req, res, next) {

    var token = (req.body && req.body.access_token) || (req.query && req.query.access_token) || req.headers['x-access-token'];

    if (!token) {
      return next();
    }

    try {
      var decoded = jwt.decode(token, app.get('jwtTokenSecret'));

      var exp = decoded.exp ? new Date(decoded.exp).getTime() : null;
      if (exp && exp <= Date.now()) {
        return next(); // expired — treat as anonymous
      }

      req.user = await Users.findOne({ _id: new ObjectId(decoded.iss) });
    } catch (err) {
      // malformed token / bad signature — fall through as anonymous
    }

    next();
  };
};
