#!/usr/bin/nodejs

var express = require('express');
var cors = require('express-cors');
var basicAuth = require('basic-auth-connect');
var bodyParser = require('body-parser');
var bcrypt = require('bcrypt');
var mailer = require('express-mailer');
var morgan = require('morgan');
var app = express();


/*
**	Starts by initilizing the connection with the Database
*/
var db = require('mongoskin').db('mongodb://localhost:27017/tiny-tank');
ObjectID = require('mongoskin').ObjectID;


Servers = db.collection('servers');
Users = db.collection('users');


/*
**	Initializes the APIs
*/
WEB_URL = 'http://localhost:3000';

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

app.use(morgan('combined'));

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
*/

require('./router.js')(app, db);
require('./background.js')(app, db);

/*
**	Starts the App
*/

var server = app.listen(process.env.PORT || 1337, function () {

	var host = server.address().address
	var port = server.address().port

	console.log('Example app listening at http://%s:%s', host, port)
})
