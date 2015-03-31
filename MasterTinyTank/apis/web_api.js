var http = require('http');
var url = require('url');
var jwt = require('jwt-simple');


WebApi = function(app, db) {

	this.list_servers = function(req, res) {
		Servers.find().toArray(function(err, result) {
			res.status(200).json({name: 'list_servers', res: result, err: err});
		});
	};

	this.register = function(req, res) {
		Users.findOne({
			$or : [{email: req.body.email.toLowerCase()}, {username: new RegExp('^'+req.body.username+'$', 'i')}]
		}, function(error, alreadyExist) {

			if (!!alreadyExist) {
				res.status(200).json({name: "register", res: false, err: "Email of Username already taken."});
			} else {

				var email_re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				var password_re = /^[\W\w]{7,99}$/;
				var username_re = /^[\w\s]{3,20}$/;

				if (!(email_re.test(req.body.email) && password_re.test(req.body.password) && username_re.test(req.body.username))) {
					res.status(200).json({name: "register", res: false, err: "Parameters aren't correct, please try again with others."});
					return false;
				}

				Users.insert({
					email: req.body.email.toLowerCase(),
					username: req.body.username,
					password: bcrypt.hashSync(req.body.password, 8),
					from: req.body.from,
					active: false,
					createdAt: new Date()
				}, function(err, result) {
					if (err || !result) {
						res.status(200).json({name: "register", res: false, err: err});
					} else {
						res.mailer.send('register', {
							to: result[0].email,
							subject: 'Thanks for your registration !',
							activ_link: WEB_URL+'/active/'+result[0]._id,
							down_link: WEB_URL+'/download',
							username: result[0].username
						}, function (err, message) {
							if (err) {
								console.log(err);
							}
						});
						res.status(200).json({name: "register", res: true, err: null});
					}
				});
			}
		});
	}

	this.login = function(req, res) {
		Users.findOne({
			$or : [{email: req.body.login.toLowerCase()}, {username: new RegExp('^'+req.body.login+'$', 'i')}]
		}, function(error, exists) {
			if (error) {
				res.status(500).json({name: "login", res: false, err: "Try again."});
			} else if (!exists) {
				res.status(200).json({name: "login", res: false, err: "Account doesn't exists."});
			} else if (exists.active == false) {
				res.status(200).json({name: "login", res: false, err: "Account not active."});
			} else {
				bcrypt.compare(req.body.password, exists.password, function(err, result) {

					if (err) {
						res.status(200).json({name: "login", res: false, err: err});
					} else if (result == false) {
						res.status(200).json({name: "login", res: false, err: "Passwords didn't match."});
					} else {

						if (!exists.token) {
							var expires = moment().add(7, 'days').toDate();
							var token = jwt.encode({
								iss: exists._id,
								exp: expires
							}, app.get('jwtTokenSecret'));
							exists.token = token;
						}

						Users.update({_id: exists._id}, {$set: {token: exists.token, updatedAt: new Date()}}, function(error, result) {

							res.status(200).json({name: "login", res: exists, err: null});

						});
					}
				});
			}
		});
	}

	this.active_account = function(req, res) {
		Users.update({_id: new ObjectID(req.body._idUser)}, {$set: {active: true}}, function(error, exists) {
			res.status(200).json({name: "active_account", res: exists, err: null});
		});
	}

	this.user_profile = function(req, res) {
		Users.findOne({
			$or : [{_id: new ObjectID(req.query._idUser)}, {username: req.query._idUser}]
		}, function(error, exists) {

			//TODO  Do the maths here. Like number of games, accuracy, etc ... Lot of stats if possible.

			res.status(200).json({name: "user_profile", res: exists, err: null});
		});
	}
};

module.exports = WebApi;
