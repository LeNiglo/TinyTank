Template.register.events({
  "submit form": function(event) {
    event.preventDefault();

    $this = $(event.target);

    var password = $this.find('input[name="password"]').val();
    var passwordConfirm = $this.find('input[name="password-v"]').val();

    if (password != passwordConfirm) {
      myAlert("Passwords must match.", "Attention,", "warning");
      return false;
    }

    var username = $this.find('input[name="username"]').val();
    var email = $this.find('input[name="email"]').val();
    var from = $this.find('select[name="from"]').val();

    var email_re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var password_re = /^[\W\w]{7,99}$/;
    var username_re = /^[\w\s]{3,20}$/;

    if (!(email_re.test(email) && password_re.test(password) && username_re.test(username))) {
      return myAlert("Parameters aren't correct, please try again with others.", "Not enought strength.", "danger");
    }

    Meteor.call("myRegister", {
      username: username,
      email: email,
      password: password,
      from: from,
      createdAt: new Date()
    }, function(error, results) {
      if (!error) {
        Router.go("login");
        return myAlert("An activation email has been sent to you. (required)", "Congratulations !", "success");
      } else {
        return myAlert(error, null, "danger");
      }
    });

  },
  "keyup input[name='password-v']": function(event) {
    if ($('form').find('input[name="password"]').val() == $(event.target).val()) {
      $('form').find('input[name="password"]').parent().addClass('has-success');
      $(event.target).parent().addClass('has-success');
      $(event.target).parent().removeClass('has-warning');
    } else {
      $('form').find('input[name="password"]').parent().removeClass('has-success');
      $(event.target).parent().removeClass('has-success');
      $(event.target).parent().addClass('has-warning');
    }
  }
});
