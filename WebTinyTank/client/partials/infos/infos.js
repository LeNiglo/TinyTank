function refreshInfos() {
  console.log('refreshInfos');
  Meteor.call("getGlobalInfos", function(error, results) {
    var res = JSON.parse(results.content);
    console.log(res);
    Session.set('infoLastRegistered', res.res.last);
    Session.set('infoNbUsers', res.res.nb_users);
  });
}

Template.infoLastUser.helpers({
  getLastUser: function() {
    return Session.get('infoLastRegistered');
  },
  getJoined: function(opt) {
    return moment(opt).fromNow();
  }
});

Template.infoNbUsers.helpers({
  getNbUsers: function() {
    return Session.get('infoNbUsers');
  }
});

Template.infos.created = function() {
  refreshInfos();
  var intervalRefreshInfos = Meteor.setInterval(refreshInfos, 120000);
}

Template.infos.destroyed = function() {
  Meteor.clearInterval(intervalRefreshInfos);
}
