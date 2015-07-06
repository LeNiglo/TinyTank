function loadUserProfile(_idUser) {

    _idUser = _idUser || window.localStorage.getItem('authID') || undefined;

    if (!_idUser) {
        return Router.go('login');
    }

    Meteor.call("getUserProfile", {_idUser: _idUser}, function (error, results) {
        if (error) {
            myAlert(error, "Warning,", "danger");
        } else if (!results) {
            Session.set('loadedUser', null);
        } else {
            var res = JSON.parse(results.content);
            Session.set('loadedUser', res.res);
            if (res.res) {
                document.title = res.res.username + "'s profile on TinyTank";
            } else {
                myAlert("Maybe you mispelled it.", "User not found", "danger");
            }
        }
    });
}


Template.profile.helpers({
    getUserProfile: function () {
        return Session.get('loadedUser');
    }
});

Template.profile.created = function () {
    Session.set('loadedUser', {loading: true});
    loadUserProfile(this.data._id);
}

Template.profileDetail.helpers({
    dateGetFrom: function (act) {
        return moment(act).fromNow();
    }
})
