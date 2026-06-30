package com.lefrantguillaume.networkComponent.dataServerComponent;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageDisconnect;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessagePlayerDelete;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessagePlayerNew;
import com.lefrantguillaume.utils.ServerConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Styve on 10/03/2015.
 *
 * REST client to the master server. Migrated from Jersey 1.x (EOL, JAXB-dependent)
 * to the JDK's built-in java.net.http.HttpClient + Jackson for JSON (de)serialization.
 */
public class DataServer {
    private static final String MASTER_SERVER = "http://tinytank.lefrantguillaume.com/api/server/";
    private static final String AUTH_USER = "T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv";
    private static final String AUTH_PASSWORD = "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo";

    private static String id = null;
    private ScheduledExecutorService updateThread;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper;

    public DataServer() {
        this.mapper = new ObjectMapper();
        // The DTOs expose plain fields (no consistent getters), so map by field.
        this.mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
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
                } else if (arg instanceof HashMap) {
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

    /**
     * POSTs {@code body} as JSON to the master server and returns the response body.
     * Throws on transport failure or a non-200 status.
     */
    private String postJson(Object body, String path) throws Exception {
        String payload = mapper.writeValueAsString(body);
        String basicAuth = Base64.getEncoder()
                .encodeToString((AUTH_USER + ":" + AUTH_PASSWORD).getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MASTER_SERVER + path))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic " + basicAuth)
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        System.out.println("sending to data server : " + payload);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
        }
        System.out.println("response from data server : " + response.body());
        return response.body();
    }

    public boolean initServer() {
        return true; /*
        try {
            InitServerSnd st = new InitServerSnd(ServerConfig.gameName, ServerConfig.tcpPort, ServerConfig.udpPort);
            InitServerRcv output = mapper.readValue(postJson(st, "init_server"), InitServerRcv.class);
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
        } catch (Exception e) {
            WindowController.addConsoleMsg("Online server not reachable: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;*/
    }

    public void updateServer() {
        try {
            UpdateServerSnd st = new UpdateServerSnd(this.id);
            UpdateServerRcv output = mapper.readValue(postJson(st, "update_last_active"), UpdateServerRcv.class);
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
            StopServerRcv output = mapper.readValue(postJson(st, "stop_server"), StopServerRcv.class);
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
            AddUserRcv output = mapper.readValue(postJson(st, "add_user"), AddUserRcv.class);
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
            DelUserRcv output = mapper.readValue(postJson(st, "remove_user"), DelUserRcv.class);
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
            String output = postJson(st, "add_game_stats");
            WindowController.addConsoleMsg("END MATCH : " + output);
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
