function loadUserProfile(_idUser) {

  _idUser = _idUser || window.localStorage.getItem('authID') || undefined;

  if (!_idUser) {
    return Router.go('login');
  }

  Meteor.call("getUserProfile", {_idUser: _idUser}, function(error, results) {
		if (error) {
			myAlert(error, "Warning,", "danger");
		} else if (!results) {
      console.log(error);
    } else {
			Session.set('loadedUser', JSON.parse(results.content).res);
		}
	});
}


Template.profile.helpers({
  getUserProfile: function() {
    return Session.get('loadedUser');
  },
  getFromRegister: function(act) {
		return moment(act).fromNow();
	}
});

Template.profile.created = function() {
    loadUserProfile(this._id);
}
