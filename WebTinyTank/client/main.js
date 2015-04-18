myAlert = function (message, title, force) {
  force = force || "warning";

  if (!message)
  return false;

  var $elements = $(Blaze.toHTMLWithData(Template.errorItem, {message: message, title: title, force: force}));

  $("#errors").append($elements);
  $('html, body').animate({
    scrollTop: 0
  }, 250);
  window.setTimeout(function() {
    $elements.remove();
  }, 5000);
  return false;
}

isUserConnectedDeps = new Deps.Dependency();

UI.registerHelper('isUserConnected', function() {
  isUserConnectedDeps.depend();
  return (window.localStorage.getItem('authToken') && window.localStorage.getItem('authID') ? true : false);
});

UI.registerHelper('getUsername',function(){
  return localStorage.getItem("authUsername");
});
