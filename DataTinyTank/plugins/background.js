var Background = function(app, db) {
  var bgTask = require('background-task').connect({ taskKey: 'checkEverything' });
  var task = {
    db: db,
    app: app
  };

  bgTask.addTask(task, function(resp){
    setInterval(function() {
      var now = moment().add(-2, 'minutes');

      Servers.remove({last_active: {$lt: now.toDate()}}, function(err, res) {
        if (res > 0)
          console.log('removed '+res+' servers.', err);
      });

    }, 30000);
  });
}

module.exports = Background;
