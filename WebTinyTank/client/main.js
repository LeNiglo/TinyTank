myAlert = function (message, title, force) {
  force = force || "warning";

  if (!message)
  return false;

  $("#errors").append(Blaze.toHTMLWithData(Template.errorItem, {message: message, title: title, force: force}));
}
