#!/usr/bin/nodejs

var express = require('express');
var cors = require('express-cors');
var basicAuth = require('basic-auth-connect');
var bodyParser = require('body-parser');
var jwt = require('jwt-simple');
var mailer = require('express-mailer');
var morgan = require('morgan');
var app = express();


/*
**	Starts by initilizing the connection with the Database
*/
bcrypt = require('bcrypt');
app.set('jwtTokenSecret', 'TheSecretStringIsMuchStrongerThanOneMillionOfTanks');
moment = require('moment');

var db = require('mongoskin').db(process.env.MONGO_URL || 'mongodb://localhost:27017/tiny-tank');
ObjectID = require('mongoskin').ObjectID;


Servers = db.collection('servers');
Users = db.collection('users');
Tanks = db.collection('tanks');
Matches = db.collection('matches');


/*
**	Initializes the APIs
*/
WEB_URL = process.env.WEB_URL || 'http://tinytank.dev';

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

app.enable('trust proxy');

app.use(cors({
	allowedOrigins: [
		"http://localhost",
		"http://tinytank.dev",
		"http://tinytank.com",
		"http://lefrantguillaume.com",
		"http://tinytank.lefrantguillaume.com"
	]
}));

/*
**	Authentification for the API
**	Replace with a real badass user and password
*/
app.use(basicAuth("T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv", "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo"));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));


/*
**	Loads the Routes
*/

require('./plugins/router.js')(app, db);
require('./plugins/background.js')(app, db);

/*
**	Starts the App
*/

var server = app.listen(process.env.PORT || 1337, function () {

	var host = server.address().address
	var port = server.address().port

})
