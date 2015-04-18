function refreshLadder() {
  console.log('refreshLadder');
  Meteor.call("getLadder", function(error, results) {
    if (error) {
      myAlert(error, "Warning,", "danger");
    } else if (results) {
      Session.set('ladder', JSON.parse(results.content).res);
      Meteor.setTimeout(displayLadder, 50);
    }
  });
}

function displayLadder() {
  var ladderColors = ["#F15841", "#A83A2A", "#4D241E"];
  var $table = $('table#ladder tbody');
  var $rows = $table.find('tr');

  for (var i = 0; i < $rows.length && i < 3; i++) {
    if (i == 0)
    $($rows[i]).find('td:last-child').html("<i style='color: gold;' class='glyphicon glyphicon-star'></i>");
    $($rows[i]).css("font-size", (28-3*i)+"px");
    $($($rows[i]).find('th.rank')[0]).css("color", ladderColors[i]);
  }
}

Template.ladder.helpers({
  getLadder: function() {
    return Session.get('ladder');
  }
});

Template.ladder.events({

});

Template.ladder.created = function() {
  refreshLadder();
  intervalRefreshLadder = Meteor.setInterval(refreshLadder, 60000);
}

Template.ladder.destroyed = function() {
  Meteor.clearInterval(intervalRefreshLadder);
};

Template.ladder.rendered = function() {
  displayLadder();
}
