Meteor.methods({
  getServersList: function() {
    this.unblock();
    try {
      return Meteor.http.call("GET", process.env.API_URL+"/list_servers", {
        params: {},
        auth: process.env.API_AUTH
      });
    } catch (e) {
      return null;
    }
  },
  getDevBlog: function() {
    this.unblock();
    try {
      return Meteor.http.call("GET", process.env.API_URL+"/dev_blog", {
        params: {},
        auth: process.env.API_AUTH
      });
    } catch (e) {
      return null;
    }
  }
});
