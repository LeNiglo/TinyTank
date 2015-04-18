var tinyHomeMessages = [
  "Have Fun",
  "Get Blasted",
  "Rank Up"
];
var tinyHomeIndex = 0;

function writeHomeMessage() {
  $('#tinyHomeMessage').text(tinyHomeMessages[tinyHomeIndex]);
}

Template.home.created = function() {
  intervalChangeHomeMessage = Meteor.setInterval(function() {
    ++tinyHomeIndex;
    if (tinyHomeIndex == tinyHomeMessages.length) {
      tinyHomeIndex = 0;
    }
    writeHomeMessage();
  }, 3000);

}

Template.home.rendered = function() {
  writeHomeMessage();
}

Template.home.destroyed = function() {
  Meteor.clearInterval(intervalChangeHomeMessage);
}
