var http = require('http');
var url = require('url');


ClientApi = function(db) {

    var Servers = db.collection('servers');

    this.init_server = function(req, res, match) {
	res.end(JSON.stringify({name: 'init_server', res: true, err: null}));
    };

};

module.exports = ClientApi;
