var http = require('http');
var url = require('url');


WebApi = function(db) {

	var Servers = db.collection('servers');
	var Blogs = db.collection('devblog');

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

};

module.exports = WebApi;
