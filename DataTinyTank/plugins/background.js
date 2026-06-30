// Reaps stale game servers: anything that hasn't sent a heartbeat in the last
// 5 minutes is removed from the registry. Runs every 30s.
var Background = function(app, db) {

  setInterval(async function() {
    var cutoff = moment().add(-5, 'minutes').toDate();

    try {
      var result = await Servers.deleteMany({last_active: {$lt: cutoff}});
      if (result.deletedCount > 0) {
        console.log('removed ' + result.deletedCount + ' stale server(s).');
      }
    } catch (err) {
      console.log('server reaper error:', err);
    }

  }, 30000);

};

module.exports = Background;
