var http = require('http');
var url = require('url');


WebApi = function(db) {

	var Servers = db.collection('servers');

	this.list_servers = function(req, res) {

		Servers.find().toArray(function(err, result) {
			res.status(200).json({name: 'list_server', res: result, err: err});
		});
	};

};

module.exports = WebApi;
