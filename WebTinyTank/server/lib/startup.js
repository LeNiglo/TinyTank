Meteor.startup(function() {
  if (!process.env.API_URL) {
    process.env.API_URL = "http://localhost:1337/web";
  } if (!process.env.API_AUTH) {
    process.env.API_URL = "username:password";
  }
});
