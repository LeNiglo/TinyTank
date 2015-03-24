var Router = function(app, db) {

  var ServerApi = require('../apis/server_api.js');
  var ClientApi = require('../apis/client_api.js');
  var WebApi = require('../apis/web_api.js');

  var serverApi = new ServerApi(app, db);
  var clientApi = new ClientApi(app, db);
  var webApi = new WebApi(app, db);

  var token_auth = require('./token_auth.js');
  app.all('*', [token_auth]);

  /*
  **	Server communication
  */

  app.post('/server/init_server', serverApi.init_server);
  app.post('/server/stop_server', serverApi.stop_server);
  app.post('/server/update_last_active', serverApi.update_last_active);
  app.post('/server/change_map', serverApi.change_map);
  app.post('/server/add_user', serverApi.add_user);
  app.post('/server/remove_user', serverApi.remove_user);

  /*
  **	Client communication
  */

  app.post('/client/login', clientApi.login);

  /*
  **	Web communication
  */

  app.post('/web/register', webApi.register);
  app.post('/web/login', webApi.login);
  app.get('/web/list_servers', webApi.list_servers);

  /*
  **	Error handling
  */

  app.all('*', function(req, res) { res.status(404).end(JSON.stringify({title:"Not Found",data:"404 Not Found"})); });

};

module.exports = Router;
