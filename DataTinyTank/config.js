/**
 * Created by leniglo on 11/06/15.
 */

var express = require('express');
var morgan = require('morgan');
var cors = require('cors');
var basicAuth = require('basic-auth-connect');

var ALLOWED_ORIGINS = [
    "http://localhost",
    "http://tinytank.dev",
    "http://tinytank.com",
    "http://lefrantguillaume.com",
    "http://tinytank.lefrantguillaume.com"
];

var Config = function (app) {

    app.use(morgan('combined'));

    app.enable('trust proxy');

    app.use(cors({
        origin: ALLOWED_ORIGINS
    }));

    /*
     **	Authentification for the API
     */
    app.use(basicAuth(process.env.AUTH_USER || "T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv",
        process.env.AUTH_PASSWORD || "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo"));

    // Body parsing is built into Express 5 (formerly the body-parser package).
    app.use(express.json());
    app.use(express.urlencoded({extended: true}));

};

module.exports = Config;
