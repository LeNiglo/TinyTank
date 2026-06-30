#!/usr/bin/env node

require('dotenv').config();

var express = require('express');
var mailer = require('./plugins/mailer.js');
var { MongoClient, ObjectId } = require('mongodb');

var app = express();
app.set('mailer', mailer);

/*
**	Globals shared across the API modules (kept for backward-compat).
*/
bcrypt = require('bcrypt');
moment = require('moment');
ObjectID = ObjectId; // legacy alias used throughout the codebase
global.ObjectId = ObjectId;

app.set('jwtTokenSecret', process.env.JWT_SECRET || 'TheSecretStringIsMuchStrongerThanOneMillionOfTanks');

WEB_URL = process.env.WEB_URL || 'http://tinytank.dev';

var MONGO_URL = process.env.MONGO_URL || 'mongodb://localhost:27017/tiny-tank';

/*
**	Connect to Mongo first, then wire up the app (the modern driver is async).
*/
(async function start() {
    var client = new MongoClient(MONGO_URL);
    await client.connect();
    var db = client.db(); // database name is taken from MONGO_URL

    Servers = db.collection('servers');
    Users = db.collection('users');
    Tanks = db.collection('tanks');
    Matches = db.collection('matches');

    require('./config.js')(app);
    require('./plugins/router.js')(app, db);
    require('./plugins/background.js')(app, db);

    var port = process.env.PORT || 1337;
    app.listen(port, function () {
        console.log('TinyTank master server listening on port ' + port);
    });
})().catch(function (err) {
    console.error('Failed to start master server:', err);
    process.exit(1);
});
