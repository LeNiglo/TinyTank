//TODO Check what tab is current active with Router.current() and add the class active to the parent li.
//TODO Implement the affix for the full screen template !

Template.header.events({
  'click #logout': function(event) {
    event.preventDefault();
    localStorage.clear();
    isUserConnectedDeps.changed();
    Router.go('home');
    return false;
  }
});
