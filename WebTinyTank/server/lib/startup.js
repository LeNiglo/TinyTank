Meteor.startup(function() {
  if (!process.env.API_URL) {
    process.env.API_URL = "http://tinytank.dev/api/web";
  } if (!process.env.API_AUTH) {
    process.env.API_AUTH = "T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv:lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo";
  }
});
