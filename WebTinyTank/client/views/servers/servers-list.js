Template.serversList.helpers({
	getServersList: function () {
		Meteor.call("getServersList", function(error, results) {
			console.log("Server", error, results);
			return results;
		});
	}
});
