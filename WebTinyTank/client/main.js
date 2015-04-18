myAlert = function (message, title, force) {
  force = force || "warning";

  if (!message)
  return false;

  $("#errors").append(Blaze.toHTMLWithData(Template.errorItem, {message: message, title: title, force: force}));
  window.setTimeout(function() {
    $(".alert-".force).alert('close');
  }, 3000);
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
