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
  },
  myRegister: function(datas) {
    this.unblock();
    try {
      return Meteor.http.call("POST", process.env.API_URL+"/register", {
        params: datas,
        auth: process.env.API_AUTH
      });
    } catch (e) {
      return null;
    }
  },
  myLogin: function() {
    this.unblock();
    try {
      return Meteor.http.call("POST", process.env.API_URL+"/login", {
        params: {},
        auth: process.env.API_AUTH
      });
    } catch (e) {
      return null;
    }
  }
});
