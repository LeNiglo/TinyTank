var http = require('http');
var url = require('url');
var jwt = require('jwt-simple');


ClientApi = function(app, db) {

  this.login = function() {
    Users.findOne({
      $or : [{email: req.body.login.toLowerCase()}, {username: new RegExp('^'+req.body.login+'$', 'i')}]
    }, function(error, exists) {

      if (!exists) {
        res.status(200).json({name: "login", res: false, err: "Account doesn't exists."});
      } else {
        bcrypt.compare(req.body.password, exists.password, function(err, res) {
          if (err) {
            res.status(200).json({name: "login", res: false, err: err});
          } else if (res == false) {
            res.status(200).json({name: "login", res: false, err: "Passwords didn't match."});
          } else {
            res.status(200).json({name: "login", res: exists, err: null});
          }
        });
      }
    });
  }

};

module.exports = ClientApi;
