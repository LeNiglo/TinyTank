ServerApi = function (app, db) {

    this.init_server = async function (req, res) {
        try {
            var result = await Servers.insertOne({
                name: req.body.gameName,
                ip: req.headers['x-forwarded-for'] || req.connection.remoteAddress,
                ports: {
                    udp: req.body.udpPort,
                    tcp: req.body.tcpPort
                },
                users: [],
                map: req.body.map,
                started_at: new Date(),
                last_active: new Date()
            });
            res.status(200).json({
                name: 'init_server',
                res: true,
                err: null,
                id: result.insertedId.toString()
            });
        } catch (err) {
            res.status(200).json({name: 'init_server', res: false, err: err.toString(), id: null});
        }
    };

    this.stop_server = async function (req, res) {
        try {
            var result = await Servers.deleteOne({_id: new ObjectId(req.body.serverId)});
            res.status(200).json({
                name: 'stop_server',
                res: (result.deletedCount !== 0),
                err: (result.deletedCount === 0 ? "serverId not found." : null)
            });
        } catch (err) {
            res.status(200).json({name: 'stop_server', res: false, err: err.toString()});
        }
    };

    this.update_last_active = async function (req, res) {
        try {
            var result = await Servers.updateOne({_id: new ObjectId(req.body.serverId)}, {$set: {last_active: new Date()}});
            res.status(200).json({
                name: 'update_last_active',
                res: (result.matchedCount !== 0),
                err: (result.matchedCount === 0 ? "serverId not found." : null)
            });
        } catch (err) {
            res.status(200).json({name: 'update_last_active', res: false, err: err.toString()});
        }
    };

    this.change_map = async function (req, res) {
        try {
            var result = await Servers.updateOne({_id: new ObjectId(req.body.serverId)}, {$set: {map: req.body.map}});
            res.status(200).json({
                name: 'change_map',
                res: (result.matchedCount !== 0),
                err: (result.matchedCount === 0 ? "serverId not found." : null)
            });
        } catch (err) {
            res.status(200).json({name: 'change_map', res: false, err: err.toString()});
        }
    };

    this.add_user = async function (req, res) {
        try {
            var result = await Servers.updateOne({_id: new ObjectId(req.body.serverId)}, {$push: {users: req.body.username}});
            res.status(200).json({
                name: 'add_user',
                res: (result.matchedCount !== 0),
                err: (result.matchedCount === 0 ? "serverId not found." : null)
            });
        } catch (err) {
            res.status(200).json({name: 'add_user', res: false, err: err.toString()});
        }
    };

    this.remove_user = async function (req, res) {
        try {
            var result = await Servers.updateOne({_id: new ObjectId(req.body.serverId)}, {$pull: {users: req.body.username}});
            res.status(200).json({
                name: 'remove_user',
                res: (result.matchedCount !== 0),
                err: (result.matchedCount === 0 ? "serverId not found." : null)
            });
        } catch (err) {
            res.status(200).json({name: 'remove_user', res: false, err: err.toString()});
        }
    };

    this.add_game_stats = async function (req, res) {
        try {
            await Matches.insertOne({
                name: req.body.gameName,
                created_at: new Date(),
                users: req.body.players
            });
            res.status(200).json({name: 'add_game_stats', res: true, err: null});
        } catch (err) {
            res.status(200).json({name: 'add_game_stats', res: false, err: err.toString()});
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

module.exports = ServerApi;
