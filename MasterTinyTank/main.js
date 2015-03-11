#!/usr/bin/nodejs

var express = require('express');
var cors = require('express-cors');
var bodyParser = require('body-parser');
var app = express();

var ServerApi = require('./server_api.js');
var ClientApi = require('./client_api.js');
var WebApi = require('./web_api.js');

var MongoClient = require('mongodb').MongoClient;

/* Starts by initilizing the connection with the Database */
var db = require('mongoskin').db('mongodb://localhost:27017/tiny-tank');

/* Initializes the APIs */
var serverApi = new ServerApi(db);
var clientApi = new ClientApi(db);
var webApi = new WebApi(db);

/* CORS Options */
app.use(cors({
	allowedOrigins: [
	"http://localhost",
	"http://lefrantguillaume.com",
	"http://tinytank.lefrantguillaume.com"
	]
}));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

/* Loads the Routes */
/* Server communication */
app.post('/server/init_server', serverApi.init_server);
app.post('/server/update_last_active', serverApi.update_last_active);
/* Client communication */
/* Web communication */
app.all('/web/list_servers', webApi.list_servers);
app.all('*', function(req, res) { res.status(404).end(JSON.stringify({title:"Not Found",data:"404 Not Found"})); });

/* Starts the App */
var server = app.listen(1337, function () {

	var host = server.address().address
	var port = server.address().port

	console.log('Example app listening at http://%s:%s', host, port)
})