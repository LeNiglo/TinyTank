#!/usr/bin/nodejs

var express = require('express');
var mailer = require('express-mailer');
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
**	Init Configs
*/

require('./config.js')(app, mailer);


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
