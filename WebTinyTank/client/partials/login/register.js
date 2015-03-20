Template.register.events({
  "submit form": function(event) {
    event.preventDefault();

    $this = $(event.target);

    var password = $this.find('input[name="password"]').val();
    var passwordConfirm = $this.find('input[name="password-v"]').val();

    if (password != passwordConfirm) {
      myAlert("Les mots de passe doivent correspondrent.", "Attention,", "warning");
      return false;
    }

    var username = $this.find('input[name="username"]').val();
    var email = $this.find('input[name="email"]').val();
    var from = $this.find('select[name="from"]').val();

    //TODO Check the username, email and password, to see if they match the correct RegExp, do the same on the server.

    Meteor.call("myRegister", {
      username: username,
      email: email,
      password: password,
      from: from,
      createdAt: new Date()
    }, function(error, results) {
      if (!error) {
        Router.go("login");
      } else {
        myAlert(error, null, "danger");
      }
    });

  }
});
