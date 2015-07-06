package com.lefrantguillaume.networkComponent.dataServerComponent;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.*;
import com.lefrantguillaume.utils.ServerConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Styve on 10/03/2015.
 */
public class DataServer {
    private static String id = null;
    private ScheduledExecutorService updateThread;

    public DataServer() {
    }

    public void doTask(Observable o, Object arg) {

        Thread me = new Thread(new Runnable() {
            @Override
            public void run() {
                if (arg instanceof MessageModel) {
                    if (arg instanceof MessagePlayerNew) {
                        DataServer.this.addUser(((MessagePlayerNew) arg).getPseudo());
                    }
                    if (arg instanceof MessagePlayerDelete || arg instanceof MessageDisconnect) {
                        DataServer.this.delUser(((MessageModel) arg).getPseudo());
                    }
                }
                else if (arg instanceof HashMap) {
                    WindowController.addConsoleMsg("DATA SERVER NEW ROUND");
                    DataServer.this.endMatch((HashMap<String, Player>) arg);
                } else {
                    WindowController.addConsoleMsg("UNKNOW INSTANCE of " + arg.getClass());
                }

            }
        });
        me.start();
        try {
            me.join(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                //TODO don't exit but display error message then quit
                System.exit(1);
            } else {
                this.id = output.getId();
                Log.info("Server id: " + this.id);

                updateThread = Executors.newScheduledThreadPool(1);
                updateThread.scheduleAtFixedRate(() -> updateServer(), 30, 120, TimeUnit.SECONDS);
            }
        } catch (ClientHandlerException e) {
            WindowController.addConsoleMsg("Online server not reachable: " + e.getCause().getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void addUser(String pseudo) {
        try {
            AddUserSnd st = new AddUserSnd(this.id, pseudo);
            ClientResponse response = this.getClientResponse(st, "add_user");
            AddUserRcv output = response.getEntity(AddUserRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delUser(String pseudo) {
        try {
            DelUserSnd st = new DelUserSnd(this.id, pseudo);
            ClientResponse response = this.getClientResponse(st, "remove_user");
            DelUserRcv output = response.getEntity(DelUserRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
            }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void endMatch(HashMap<String, Player> players) {
        try {
            SendStatsSnd st = new SendStatsSnd(this.id, ServerConfig.gameName, players);
            ClientResponse response = this.getClientResponse(st, "add_game_stats");
            // SendStatsRcv output = response.getEntity(SendStatsRcv.class);
            String output = response.getEntity(String.class);
            WindowController.addConsoleMsg("END MATCH : " + output);
            // if (!output.getRes()) {
            //    Log.error("Master server error: " + output.getErr());
            // }
        } catch (Exception e) {
            Log.error("Master: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }
}
