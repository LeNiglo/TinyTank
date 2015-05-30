var http = require('http');
var url = require('url');


ClientApi = function(app, db) {

  this.list_servers = function (req, res) {
    Servers.find().toArray(function (err, result) {
      res.status(200).json({name: 'list_servers', res: JSON.stringify(result), err: err});
    });
  };

  this.login = function(req, res) {
    Users.findOne({
      $or : [{email: req.body.login.toString().toLowerCase()}, {username: new RegExp('^'+req.body.login.toString()+'$', 'i')}]
    }, function(error, exists) {

      if (!exists) {
        res.status(200).json({name: "login", res: null, err: "Account doesn't exists."});
      } else {
        bcrypt.compare(req.body.password.toString(), exists.password, function(err, result) {
          if (err) {
            res.status(200).json({name: "login", res: null, err: err});
          } else if (result == false) {
            res.status(200).json({name: "login", res: null, err: "Passwords didn't match."});
          } else {
            if (!app.get('jwtTokenSecret') == req.body.secret.toString()) {
              res.status(200).json({name: "login", res: null, err: "You are a cheater."});
            } else {
              res.status(200).json({name: "login", res: JSON.stringify(exists), err: null});
            }
          }
        });
      }
    });
  }

  this.get_tank_list = function(req, res) {
    Tanks.find().toArray(function(err, result) {
      res.status(200).json({name: 'get_tank_list', res: result, err: err});
    });
  }

};

module.exports = ClientApi;
