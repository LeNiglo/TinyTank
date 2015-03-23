Template.login.events({
  "submit form": function(event) {
    event.preventDefault();

    $this = $(event.target);

    var password = $this.find('input[name="password"]').val();
    var email = $this.find('input[name="email"]').val();

    Meteor.call("myLogin", {
      email: email,
      password: password
    }, function(error, results) {
      if (error) {
        myAlert(error, "Login failed", "danger");
      } else {
        //TODO change by profile page and add username to the myAlert
        Router.go("home");
        myAlert("", "Welcome Back !", "success");
      }
    });

  }
});
