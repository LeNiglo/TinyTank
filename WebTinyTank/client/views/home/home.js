var tinyHomeMessages = [
  "Have Fun",
  "Get Blasted",
  "Rank Up"
];
var tineHomeColors = [
  "#7F0000",
  "#CC0000",
  "#7F2626"
]
var tinyHomeIndex = 0;

function writeHomeMessage() {
  $('#tinyHomeMessage').css("color", tineHomeColors[tinyHomeIndex]).shuffleLetters({
    "text": tinyHomeMessages[tinyHomeIndex]
  });
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
