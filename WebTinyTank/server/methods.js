Meteor.methods({
  getUserProfile: function(datas) {
    this.unblock();
    return myShortHttp("GET", "/user_profile", datas);
  },
  getGlobalInfos: function() {
    this.unblock();
    return myShortHttp("GET", "/get_infos");
  },
  getServersList: function() {
    this.unblock();
    return myShortHttp("GET", "/list_servers");
  },
  getLadder: function() {
    this.unblock();
    return myShortHttp("GET", "/ladder");
  },
  myRegister: function(datas) {
    this.unblock();
    return myShortHttp("POST", "/register", datas);
  },
  active_account: function(datas) {
    this.unblock();
    return myShortHttp("POST", "/active_account", datas);
  },
  myLogin: function(datas) {
    this.unblock();
    return myShortHttp("POST", "/login", datas);
  }
});

function myShortHttp(method, url, datas) {
  try {
    return Meteor.http.call(method, process.env.API_URL+url, {
      params: datas,
      auth: process.env.API_AUTH
    });
  } catch (e) {
    console.log(method, url, datas, e);
    return null;
  }
}
