Template.header.events({
    'click #logout': function (event) {
        event.preventDefault();
        localStorage.clear();
        isUserConnectedDeps.changed();
        Router.go('home');
        return false;
    }
});