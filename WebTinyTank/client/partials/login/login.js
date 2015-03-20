Template.login.events({
  "submit form": function(event) {
    event.preventDefault();

    $this = $(event.target);

    var password = $this.find('input[name="password"]').val();
    var email = $this.find('input[name="email"]').val();

    //TODO Check the email and password, to see if they match the correct RegExp, to the same on the server.

    Meteor.call("myLogin", {
      email: email,
      password: password
    }, function(error, results) {
      console.log("login", error, results);
      if (error) {
        myAlert('')
      } else {

      }
    });

  }
});
