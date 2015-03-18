#!/usr/bin/nodejs

var express = require('express');
var cors = require('express-cors');
var basicAuth = require('basic-auth-connect');
var bodyParser = require('body-parser');
var app = express();

var ServerApi = require('./server_api.js');
var ClientApi = require('./client_api.js');
var WebApi = require('./web_api.js');


/*
** Starts by initilizing the connection with the Database
*/
var db = require('mongoskin').db('mongodb://localhost:27017/tiny-tank');
ObjectID = require('mongoskin').ObjectID

/*
** Initializes the APIs
*/

var serverApi = new ServerApi(db);
var clientApi = new ClientApi(db);
var webApi = new WebApi(db);

app.use(cors({
	allowedOrigins: [
	"http://localhost",
	"http://lefrantguillaume.com",
	"http://tinytank.lefrantguillaume.com"
	]
}));

/*
** Authentification for the API
** Replace with a real badass user and password
*/
app.use(basicAuth('username', 'password'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));


/*
** Loads the Routes
**
** Server communication
*/

app.post('/server/init_server', serverApi.init_server);
app.post('/server/update_last_active', serverApi.update_last_active);

/*
** Client communication
*/

/*
** Web communication
*/

app.all('/web/list_servers', webApi.list_servers);
app.all('/web/dev_blog', webApi.dev_blog);

/*
** Error handling
*/

app.all('*', function(req, res) { res.status(404).end(JSON.stringify({title:"Not Found",data:"404 Not Found"})); });

/*
** Starts the App
*/

var server = app.listen(process.env.PORT || 1337, function () {

	var host = server.address().address
	var port = server.address().port

	console.log('Example app listening at http://%s:%s', host, port)
})
