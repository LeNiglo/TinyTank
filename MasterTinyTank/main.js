#!/usr/bin/nodejs

var express = require('express');
var cors = require('express-cors');
var basicAuth = require('basic-auth-connect');
var bodyParser = require('body-parser');
var bcrypt = require('bcrypt');
var mailer = require('express-mailer');
var app = express();

var ServerApi = require('./server_api.js');
var ClientApi = require('./client_api.js');
var WebApi = require('./web_api.js');


/*
**	Starts by initilizing the connection with the Database
*/
var db = require('mongoskin').db('mongodb://localhost:27017/tiny-tank');
var ObjectID = require('mongoskin').ObjectID


var Servers = db.collection('servers');
var Users = db.collection('users');


/*
**	Initializes the APIs
*/
var WEB_URL = 'http://localhost:3000';
var serverApi = new ServerApi(db);
var clientApi = new ClientApi(db);
var webApi = new WebApi(db);

/*
**	Init Mailer
*/

mailer.extend(app, {
	from: 'TinyTank <no-reply@tinytank.lefrantguillaume.com>',
	host: 'smtp.gmail.com',
	secureConnection: true,
	port: 465,
	transportMethod: 'SMTP',
	auth: {
		user: 'webpatalot@gmail.com',
		pass: 'motdepassesecure'
	}
});
app.set('views', __dirname + '/emails');
app.set('view engine', 'jade');

app.use(cors({
	allowedOrigins: [
		"http://localhost",
		"http://lefrantguillaume.com",
		"http://tinytank.lefrantguillaume.com"
	]
}));

/*
**	Authentification for the API
**	Replace with a real badass user and password
*/
app.use(basicAuth('username', 'password'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));


/*
**	Loads the Routes
**
**	Server communication
*/

app.post('/server/init_server', serverApi.init_server);
app.post('/server/update_last_active', serverApi.update_last_active);

/*
**	Client communication
*/

/*
**	Web communication
*/

app.post('/web/register', webApi.register);
app.post('/web/login', webApi.login);
app.get('/web/list_servers', webApi.list_servers);

/*
**	Error handling
*/

app.all('*', function(req, res) { res.status(404).end(JSON.stringify({title:"Not Found",data:"404 Not Found"})); });

/*
**	Starts the App
*/

var server = app.listen(process.env.PORT || 1337, function () {

	var host = server.address().address
	var port = server.address().port

	console.log('Example app listening at http://%s:%s', host, port)
})
