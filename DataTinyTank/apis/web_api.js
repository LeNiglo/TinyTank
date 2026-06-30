var jwt = require('jwt-simple');


WebApi = function (app, db) {

    this.list_servers = async function (req, res) {
        try {
            var result = await Servers.find().toArray();
            res.status(200).json({name: 'list_servers', res: result, err: null});
        } catch (err) {
            res.status(200).json({name: 'list_servers', res: null, err: err.toString()});
        }
    };

    this.get_infos = async function (req, res) {
        try {
            var users = await Users.find({}).sort({createdAt: -1}).toArray();
            var nb_users = users.length;
            var last = null;
            if (nb_users > 0)
                last = {
                    username: users[0].username,
                    _id: users[0]._id,
                    createdAt: users[0].createdAt
                };
            res.status(200).json({name: 'get_infos', res: {last: last, nb_users: nb_users}, err: null});
        } catch (err) {
            res.status(200).json({name: 'get_infos', res: null, err: err.toString()});
        }
    };

    this.ladder = function (req, res) {
        //TODO Find a way to calculate through databases !
        var ladder = [
            {rank: 1, username: "LeNiglo", _id: "551ac68a894afa47ae65e215", gamesPlayed: 1337, killCount: 3214, accuracy: 85.2},
            {rank: 2, username: "Switi", _id: "551ac68a894afa47ae65e214", gamesPlayed: 1302, killCount: 729, accuracy: 87.8},
            {rank: 3, username: "DraymZz", _id: "551ac68a894afa47ae65e213", gamesPlayed: 668, killCount: 520, accuracy: 83.0},
            {rank: 4, username: "La Chose", _id: "551ac68a894afa47ae65e212", gamesPlayed: 932, killCount: 414, accuracy: 91.2},
            {rank: 5, username: "ZaZa", _id: "551ac68a894afa47ae65e211", gamesPlayed: 204, killCount: 399, accuracy: 80.9}
        ];
        res.status(200).json({name: 'ladder', res: ladder, err: null});
    };

    this.register = async function (req, res) {
        try {
            var alreadyExist = await Users.findOne({
                $or: [
                    {email: req.body.email.toLowerCase()},
                    {username: new RegExp('^' + req.body.username + '$', 'i')}
                ]
            });

            if (alreadyExist) {
                return res.status(200).json({name: "register", res: false, err: "Email of Username already taken."});
            }

            var email_re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            var password_re = /^[\W\w]{7,99}$/;
            var username_re = /^[\w\s]{3,20}$/;

            if (!(email_re.test(req.body.email) && password_re.test(req.body.password) && username_re.test(req.body.username))) {
                return res.status(200).json({
                    name: "register",
                    res: false,
                    err: "Parameters aren't correct, please try again with others."
                });
            }

            var email = req.body.email.toLowerCase();
            var username = req.body.username;
            var result = await Users.insertOne({
                email: email,
                username: username,
                password: bcrypt.hashSync(req.body.password, 8),
                from: req.body.from,
                active: false,
                createdAt: new Date()
            });

            app.get('mailer').send('register', {
                to: email,
                subject: 'Thanks for your registration !',
                activ_link: WEB_URL + '/active/' + result.insertedId,
                down_link: WEB_URL + '/download',
                username: username
            }, function (err, message) {
                if (err) {
                    console.log(err);
                }
            });

            res.status(200).json({name: "register", res: true, err: null});
        } catch (err) {
            res.status(200).json({name: "register", res: false, err: err.toString()});
        }
    };

    this.login = async function (req, res) {
        try {
            var exists = await Users.findOne({
                $or: [
                    {email: req.body.login.toLowerCase()},
                    {username: new RegExp('^' + req.body.login + '$', 'i')}
                ]
            });

            if (!exists) {
                return res.status(200).json({name: "login", res: false, err: "Account doesn't exists."});
            }
            if (exists.active == false) {
                return res.status(200).json({name: "login", res: false, err: "Account not active."});
            }

            var match = await bcrypt.compare(req.body.password, exists.password);
            if (!match) {
                return res.status(200).json({name: "login", res: false, err: "Passwords didn't match."});
            }

            if (!exists.token) {
                var expires = moment().add(7, 'days').toDate();
                exists.token = jwt.encode({
                    iss: exists._id,
                    exp: expires
                }, app.get('jwtTokenSecret'));
            }

            await Users.updateOne({_id: exists._id}, {
                $set: {
                    token: exists.token,
                    updatedAt: new Date()
                }
            });

            res.status(200).json({name: "login", res: exists, err: null});
        } catch (err) {
            res.status(500).json({name: "login", res: false, err: "Try again."});
        }
    };

    this.active_account = async function (req, res) {
        try {
            var result = await Users.updateOne({_id: new ObjectId(req.body._idUser)}, {$set: {active: true}});
            res.status(200).json({name: "active_account", res: result, err: null});
        } catch (err) {
            res.status(200).json({name: "active_account", res: null, err: err.toString()});
        }
    };

    this.user_profile = async function (req, res) {
        try {
            var objId = null;
            var regUn = new RegExp('^' + req.query._idUser + '$', 'i');
            try {
                objId = new ObjectId(req.query._idUser);
            } catch (e) {
                // not a valid ObjectId — fall back to username lookup only
            }

            var exists = await Users.findOne({
                $or: [{_id: objId}, {username: regUn}]
            });

            if (!exists) {
                return res.status(200).json({name: "user_profile", res: null, err: "User not found."});
            }

            exists.stats = {
                gamesPlayed: 0, kills: 0, deaths: 0, score: 0, shotsFired: 0, shotsHit: 0,
                killsPG: 0, deathsPG: 0, scorePG: 0, shotsFiredPG: 0, shotsHitPG: 0
            };

            var docs = await Matches.find({'users.id': exists._id.toString()}).toArray();

            if (docs.length > 0) {
                for (var i = 0; i < docs.length; i++) {
                    for (var j = 0; j < docs[i].users.length; j++) {
                        if (docs[i].users[j].id == exists._id.toString()) {
                            exists.stats.kills += docs[i].users[j].kills;
                            exists.stats.deaths += docs[i].users[j].deaths;
                            exists.stats.score += docs[i].users[j].currentScore;
                            exists.stats.shotsFired += docs[i].users[j].nbShots;
                            exists.stats.shotsHit += docs[i].users[j].nbHits;
                            break;
                        }
                    }
                }

                exists.stats.killsPG = exists.stats.kills / docs.length;
                exists.stats.deathsPG = exists.stats.deaths / docs.length;
                exists.stats.scorePG = exists.stats.score / docs.length;
                exists.stats.shotsFiredPG = exists.stats.shotsFired / docs.length;
                exists.stats.shotsHitPG = exists.stats.shotsHit / docs.length;
                exists.stats.gamesPlayed = docs.length;
            }
            res.status(200).json({name: "user_profile", res: exists, err: null});
        } catch (err) {
            res.status(200).json({name: "user_profile", res: null, err: err.toString()});
        }
    };

    this.get_tank_list = async function (req, res) {
        try {
            var result = await Tanks.find().toArray();
            res.status(200).json({name: 'get_tank_list', res: result, err: null});
        } catch (err) {
            res.status(200).json({name: 'get_tank_list', res: null, err: err.toString()});
        }
    };
};

module.exports = WebApi;
