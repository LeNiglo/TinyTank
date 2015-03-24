var Background = function(app, db) {
  var bgTask = require('background-task').connect({ taskKey: 'checkEverything' });
  var task = {
    db: db,
    app: app
  };

  bgTask.addTask(task, function(resp){
    setInterval(function() {
      var now = moment().add(-15, 'minutes');

      Servers.remove({last_active: {$lt: now.toDate()}}, function(err, res) {
        console.log('removed '+res+' servers.', err);
      });

    }, 60000);
  });
}

module.exports = Background;
