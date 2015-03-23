var http = require('http');
var url = require('url');


ServerApi = function(db) {

	this.init_server = function(req, res) {
		Servers.insert({
			name: req.body.gameName,
			ip: req.ip,
			ports: {
				udp: req.body.udpPort,
				tcp: req.body.tcpPort
			},
			users: [],
			map: req.body.map,
			started_at: new Date(),
			last_active: new Date()
		}, function(err, result) {
			res.status(200).json({name: 'init_server', res: (err ? false : true), err: (err ? err.toString() : null), id: result[0]._id.toString()});
		});
	};

	this.update_last_active = function(req, res) {
		Servers.update({_id: new ObjectID(req.body.serverId)}, {$set: {last_active: new Date()}}, function(err, result) {
			res.status(200).json({name: 'update_last_active', res: (result == 0 ? false : true), err: (err ? err.toString() : (result == 0 ? "serverId not found." : null))});
		});
	}

	this.change_map = function(req, res) {
		Servers.update({_id: new ObjectID(req.body.serverId)}, {$set: {map: req.body.map}}, function(err, result) {
			res.status(200).json({name: 'update_last_active', res: (result == 0 ? false : true), err: (err ? err.toString() : (result == 0 ? "serverId not found." : null))});
		});
	}

	this.add_user = function(req, res) {
		Servers.update({_id: new ObjectID(req.body.serverId)}, {$push: {users: req.body.username}}, function(err, result) {
			res.status(200).json({name: 'update_last_active', res: (result == 0 ? false : true), err: (err ? err.toString() : (result == 0 ? "serverId not found." : null))});
		});
	}

	this.remove_user = function(req, res) {
		Servers.update({_id: new ObjectID(req.body.serverId)}, {$pull: {users: req.body.username}}, function(err, result) {
			res.status(200).json({name: 'update_last_active', res: (result == 0 ? false : true), err: (err ? err.toString() : (result == 0 ? "serverId not found." : null))});
		});
	}

};

module.exports = ServerApi;
