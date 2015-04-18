function refreshListServers() {
	console.log('refreshListServers');
	Meteor.call("getServersList", function(error, results) {
		if (error) {
			myAlert(error, "Warning,", "danger");
		} else if (results) {
			Session.set('serverList', JSON.parse(results.content).res);
		}
	});
}

Template.serversList.helpers({
	getServersList: function () {
		return Session.get('serverList');
	}
});

Template.serverItem.helpers({
	getUptime: function(act) {
		return moment(act).fromNow();
	},
	getPlayerCount: function(users) {
		return users.length;
	}
});

Template.serversList.created = function() {
	refreshListServers();
	intervalRefreshListServer = Meteor.setInterval(refreshListServers, 60000);
}

Template.serversList.destroyed = function() {
	Meteor.clearInterval(intervalRefreshListServer);
};
