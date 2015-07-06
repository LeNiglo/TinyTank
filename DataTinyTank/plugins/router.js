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
  app.post('/server/add_game_stats', serverApi.add_game_stats);
  app.get('/server/get_tank_list', serverApi.get_tank_list);

  /*
  **	Client communication
  */

  app.post('/client/login', clientApi.login);
  app.post('/client/list_servers', clientApi.list_servers);
  app.get('/client/user_profile', clientApi.user_profile);
  app.get('/client/get_tank_list', serverApi.get_tank_list);

  /*
  **	Web communication
  */

  app.post('/web/register', webApi.register);
  app.post('/web/login', webApi.login);
  app.post('/web/active_account', webApi.active_account);
  app.get('/web/list_servers', webApi.list_servers);
  app.get('/web/ladder', webApi.ladder);
  app.get('/web/user_profile', webApi.user_profile);
  app.get('/web/get_infos', webApi.get_infos);
  app.get('/web/get_tank_list', serverApi.get_tank_list);

  /*
  **	Error handling
  */

  app.all('*', function(req, res) { res.status(404).end(JSON.stringify({title:"Not Found",data:"404 Not Found"})); });

};

module.exports = Router;
