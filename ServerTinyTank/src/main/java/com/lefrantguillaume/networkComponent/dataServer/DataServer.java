package com.lefrantguillaume.networkComponent.dataServer;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.utils.ServerConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Styve on 10/03/2015.
 */
public class DataServer {
    private String id = null;
    private ScheduledExecutorService updateThread;

    public DataServer() {
    }

    public void doTask(Observable o, Object arg){

    }
    private ClientResponse getClientResponse(Object st, String path) {
        String masterServer = "http://tinytank.lefrantguillaume.com/api/server/";

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv", "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo"));
        WebResource webResource = client.resource(masterServer + path);
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, st);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    public boolean initServer() {
        try {
            InitServerSnd st = new InitServerSnd(ServerConfig.gameName, ServerConfig.tcpPort, ServerConfig.udpPort);
            ClientResponse response = this.getClientResponse(st, "init_server");
            InitServerRcv output = response.getEntity(InitServerRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
                //TODO no exit 0 but display error message then quit
                System.exit(0);
            } else {
                this.id = output.getId();
                Log.info("Server id: " + this.id);

                updateThread = Executors.newScheduledThreadPool(5);
                updateThread.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        updateServer();
                    }
                }, 300, 300, TimeUnit.SECONDS);
            }
        } catch (ClientHandlerException e) {
            WindowController.addConsoleMsg("Online server not reachable: " + e.getCause().getMessage());
            return false;
        }
        return true;
    }

    public void updateServer() {
        try {
            UpdateServerSnd st = new UpdateServerSnd(this.id);
            ClientResponse response = this.getClientResponse(st, "update_last_active");
            UpdateServerRcv output = response.getEntity(UpdateServerRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            } else {
                Log.info("Sent alive signal to data server.");
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
        }
    }

    public void stopServer() {
        try {
            updateThread.shutdown();
            StopServerSnd st = new StopServerSnd(this.id);
            ClientResponse response = this.getClientResponse(st, "stop_server");
            StopServerRcv output = response.getEntity(StopServerRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
        }
    }

    public void addUser(String pseudo) {
        try {
            updateThread.shutdown();
            AddUserSnd st = new AddUserSnd(this.id, pseudo);
            ClientResponse response = this.getClientResponse(st, "add_user");
            AddUserRcv output = response.getEntity(AddUserRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
        }
    }

    public void delUser(String pseudo) {
        try {
            updateThread.shutdown();
            DelUserSnd st = new DelUserSnd(this.id, pseudo);
            ClientResponse response = this.getClientResponse(st, "remove_user");
            DelUserRcv output = response.getEntity(DelUserRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
        }
    }

    public void endMatch(List<Player> player) {
        try {
            updateThread.shutdown();
            SendStatsSnd st = new SendStatsSnd(this.id, player);
            ClientResponse response = this.getClientResponse(st, "add_game_stats");
            SendStatsRcv output = response.getEntity(SendStatsRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            } else {
                String users = "";
                boolean first = true;
                for (Player entry : player) {
                    if (first) {
                        first = false;
                    } else {
                        users += ", ";
                    }
                    users += entry.getPseudo();
                }
                WindowController.addConsoleMsg("Sent player stats for " + users + ".");
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
        }
    }
}
