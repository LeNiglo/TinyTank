Template.login.events({
  "submit form": function(event) {
    event.preventDefault();

    $this = $(event.target);

    var password = $this.find('input[name="password"]').val();
    var email = $this.find('input[name="email"]').val();

    if (!email.length > 0 || !password.length > 0) {
      return false;
    }

    Meteor.call("myLogin", {
      login: email,
      password: password
    }, function(error, results) {
      if (error) {
        myAlert(error, "Login failed", "danger");
      } else {
        console.log(error, results);
        Session.set('authToken', results.data.res.token);
        console.log(Session.get('authToken'));
        //TODO change by profile page and add username to the myAlert
        //Router.go("home");
        myAlert("", "Welcome Back !", "success");
      }
    });

  }
});
