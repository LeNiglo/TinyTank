/**
 * Created by leniglo on 11/06/15.
 */

var morgan = require('morgan');
var jwt = require('jwt-simple');
var cors = require('express-cors');
var basicAuth = require('basic-auth-connect');
var bodyParser = require('body-parser');

var Config = function (app, mailer) {

    mailer.extend(app, {
        from: 'TinyTank <no-reply@tinytank.lefrantguillaume.com>',
        host: process.env.SMTP_HOST,
        port: process.env.SMTP_PORT,
        transportMethod: 'SMTP',
        auth: {
            user: process.env.SMTP_USER,
            pass: process.env.SMTP_PASSWORD
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
     */
    app.use(basicAuth(process.env.AUTH_USER || "T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv",
        process.env.AUTH_PASSWORD || "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo"));

    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({extended: true}));

};

module.exports = Config;
