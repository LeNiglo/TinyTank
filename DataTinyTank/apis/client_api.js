ClientApi = function (app, db) {

    this.list_servers = async function (req, res) {
        try {
            var result = await Servers.find().toArray();
            res.status(200).json({name: 'list_servers', res: JSON.stringify(result), err: null});
        } catch (err) {
            res.status(200).json({name: 'list_servers', res: null, err: err.toString()});
        }
    };

    this.login = async function (req, res) {
        try {
            var exists = await Users.findOne({
                $or: [
                    {email: req.body.login.toString().toLowerCase()},
                    {username: new RegExp('^' + req.body.login.toString() + '$', 'i')}
                ]
            });

            if (!exists) {
                return res.status(200).json({name: "login", res: null, err: "Account doesn't exists."});
            }

            var match = await bcrypt.compare(req.body.password.toString(), exists.password);
            if (!match) {
                return res.status(200).json({name: "login", res: null, err: "Passwords didn't match."});
            }

            // NOTE: the legacy `secret` shared-token check here was a no-op (it never
            // triggered and threw on missing input), so it has been removed.

            var inGame = await Servers.findOne({users: exists.username});
            if (inGame) {
                return res.status(200).json({name: "login", res: null, err: "You are already in game."});
            }

            res.status(200).json({name: "login", res: JSON.stringify(exists), err: null});
        } catch (err) {
            res.status(200).json({name: "login", res: null, err: err.toString()});
        }
    };

};

module.exports = ClientApi;
