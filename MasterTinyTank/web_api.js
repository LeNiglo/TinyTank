var http = require('http');
var url = require('url');


WebApi = function(db) {

	var Servers = db.collection('servers');
	var Blogs = db.collection('devblog');
	var Users = db.collection('users');

	this.list_servers = function(req, res) {

		Servers.find().toArray(function(err, result) {
			res.status(200).json({name: 'list_servers', res: result, err: err});
		});
	};

	this.dev_blog = function(req, res) {
		Blogs.find().toArray(function(err, result) {
			res.status(200).json({name: "dev_blog", res: result, err: err});
		});
	}

	this.register = function(req, res) {
		Users.findOne({
			$or : [{email: req.body.email.toLowerCase()}, {username: new RegExp('^'+req.body.username+'$', 'i')}]
		}, function(error, alreadyExist) {

			if (!!alreadyExist) {
				res.status(200).json({name: "register", res: false, err: "Email of Username already taken."});
			} else {

				//TODO Check here if email, username and password are correct ...

				console.log(req.body);

				Users.insert({
					email: req.body.email.toLowerCase(),
					username: req.body.username,
					password: bcrypt.hashSync(req.body.password, 8),
					from: req.body.from,
					active: false,
					createdAt: new Date()
				}, function(err, result) {
					if (err && !result) {
						res.status(200).json({name: "register", res: false, err: err});
					} else {
						res.mailer.send('register', {
							to: result[0].email,
							subject: 'Thanks for your registration !',
							activ_link: WEB_URL+'/account/active',
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

			if (!exists) {
				res.status(200).json({name: "login", res: false, err: "Account doesn't exists."});
			} else {
				bcrypt.compare(req.body.password, exists.password, function(err, res) {
					if (err) {
						console.log(err, res);
						res.status(200).json({name: "login", res: false, err: err});
					} else {
						res.status(200).json({name: "login", res: true, err: null});
					}
				});
			}
		});
	}
};

module.exports = WebApi;
