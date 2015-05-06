var http = require('http');
var url = require('url');


ClientApi = function(app, db) {

  this.list_servers = function (req, res) {
    Servers.find().toArray(function (err, result) {
      res.status(200).json({name: 'list_servers', res: result, err: err});
    });
  };

  this.login = function() {
    Users.findOne({
      $or : [{email: req.body.login.toLowerCase()}, {username: new RegExp('^'+req.body.login+'$', 'i')}]
    }, function(error, exists) {

      if (!exists) {
        res.status(200).json({name: "login", res: null, err: "Account doesn't exists."});
      } else {
        bcrypt.compare(req.body.password, exists.password, function(err, res) {
          if (err) {
            res.status(200).json({name: "login", res: null, err: err});
          } else if (res == false) {
            res.status(200).json({name: "login", res: null, err: "Passwords didn't match."});
          } else {
            if (!req.body.secret.equals(app.get('jwtTokenSecret'))) {
              res.status(200).json({name: "login", res: null, err: "You are a cheater."});
            } else {
              res.status(200).json({name: "login", res: exists, err: null});
            }
          }
        });
      }
    });
  }

};

module.exports = ClientApi;
