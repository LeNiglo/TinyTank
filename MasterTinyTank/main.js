#!/usr/bin/nodejs

var http = require('http');
var fs = require('fs');
var url = require('url');
var Router = require('routes');
var router = new Router();
var ServerApi = require('./server_api.js');
var ClientApi = require('./client_api.js');
var WebApi = require('./web_api.js');
var MongoClient = require('mongodb').MongoClient;

var clean_mongo = function(cb) {
    MongoClient.connect('mongodb://localhost:27017/tiny-tank', function(err, db) {
	if (err) {
	    cb(err, null);
	} else {
	    cb(null, db);
	}
    });
};

var notFound = function(req, res, match) {
    res.statusCode = 404;
    res.end(JSON.stringify({title: 'Not Found', data: '404 Not Found'}));
};

/* Starts by initilizing the connection with the Database */
clean_mongo(function(err, db) {

    if (err) {
	console.log("Could not connect to MongoDB.");
	return false;
    } else {
	/* Initializes the APIs */
	var serverApi = new ServerApi(db);
	var clientApi = new ClientApi(db);
	var webApi = new WebApi(db);

	/* HTTP options */
	var allowed_origins = [
	    "http://localhost",
	    "http://lefrantguillaume.com",
	    "http://tinytank.lefrantguillaume.com"
	];

	/* Loads the Routes */
	router.addRoute('/servers/init_server', serverApi.init_server);
	router.addRoute('/*', notFound);

	/* Starts the HTTP server */
	var server = http.createServer(function (req, res) {
	    var path = url.parse(req.url).pathname;
	    var match = router.match(path);

	    /* Enables CORS */
	    if (allowed_origins.indexOf(req.headers.origin) >= 0) {
		res.setHeader('Access-Control-Allow-Origin', req.headers.origin);
		res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
		res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
		res.setHeader('Access-Control-Allow-Credentials', true);
	    }

	    res.statusCode = 200;
	    res.setHeader('Content-Type', 'application/json; charset=utf-8');

	    console.log(path);
	    match.fn(req, res, match);
	}).listen(6668, function() {
	    console.log('http server listening on port ' + this.address().port);
	});
    }

});
