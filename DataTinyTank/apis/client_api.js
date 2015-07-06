var http = require('http');
var url = require('url');


ClientApi = function (app, db) {

    this.list_servers = function (req, res) {
        Servers.find().toArray(function (err, result) {
            res.status(200).json({name: 'list_servers', res: JSON.stringify(result), err: err});
        });
    };

    this.login = function (req, res) {
        Users.findOne({
            $or: [{email: req.body.login.toString().toLowerCase()}, {username: new RegExp('^' + req.body.login.toString() + '$', 'i')}]
        }, function (error, exists) {

            if (!exists) {
                res.status(200).json({name: "login", res: null, err: "Account doesn't exists."});
            } else {
                bcrypt.compare(req.body.password.toString(), exists.password, function (err, result) {
                    if (err) {
                        res.status(200).json({name: "login", res: null, err: err});
                    } else if (result == false) {
                        res.status(200).json({name: "login", res: null, err: "Passwords didn't match."});
                    } else {
                        if (!app.get('jwtTokenSecret') == req.body.secret.toString()) {
                            res.status(200).json({name: "login", res: null, err: "You are a cheater."});
                        } else {
                            Servers.findOne({users: exists.username}, function (error, result) {
                                if (!error && result) {
                                    res.status(200).json({name: "login", res: null, err: "You are already in game."});
                                } else if (error) {
                                    res.status(200).json({name: "login", res: null, err: error});
                                } else {
                                    res.status(200).json({name: "login", res: JSON.stringify(exists), err: null});
                                }
                            });
                        }
                    }
                });
            }
        });
    };

    this.user_profile = function (req, res) {
        var objId = null;
        var regUn = new RegExp('^' + req.query._idUser + '$', 'i');
        try {
            objId = new ObjectID(req.query._idUser);
        } catch (e) {
            console.log(e);
        }
        Users.findOne({
            $or: [{_id: objId}, {username: regUn}]
        }, function (error, exists) {

            exists.stats = {
                kills: 0,
                deaths: 0,
                score: 0,
                shotsFired: 0,
                shotsHit: 0,
                killsPG: 0,
                deathsPG: 0,
                scorePG: 0,
                shotsFiredPG: 0,
                shotsHitPG: 0
            };

            Matches.find({'users.id': exists._id.toString()}).toArray(function (error, docs) {

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

                res.status(200).json({name: "user_profile", res: exists, err: null});

            });
        });
    };

    this.get_tank_list = function (req, res) {
        Tanks.find().toArray(function (err, result) {
            res.status(200).json({name: 'get_tank_list', res: result, err: err});
        });
    };

};

module.exports = ClientApi;
