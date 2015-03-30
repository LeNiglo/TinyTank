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
      console.log(error, results);
      if (error) {
        myAlert(error, "Server Error, ", "danger");
      } else if (results.data.res == false) {
        myAlert(results.data.err, "Login failed, ", "danger");
      } else {
        localStorage.setItem('authToken', results.data.res.token);
        localStorage.setItem('authID', results.data.res._id);
        localStorage.setItem('authUsername', results.data.res.username);
        //TODO change by profile page and add username to the myAlert
        isUserConnectedDeps.changed();
        Router.go("profile");
        myAlert("Happy to see you, "+results.data.res.username+ " !", "Welcome Back !", "success");
      }
    });

  }
});
