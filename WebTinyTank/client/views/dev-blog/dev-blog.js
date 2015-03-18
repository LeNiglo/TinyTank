Template.devBlog.helpers({
	getDevBlog: function () {
		Meteor.call("getDevBlog", function(error, results) {
			console.log("DevBlog", error, results);
      return results;
		});
	}
});
