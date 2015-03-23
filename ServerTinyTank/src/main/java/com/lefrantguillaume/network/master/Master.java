package com.lefrantguillaume.network.master;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.network.Network;
import com.lefrantguillaume.utils.ServerConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.concurrent.*;

/**
 * Created by Styve on 10/03/2015.
 */
public class Master {
    private String id = null;

    public Master() {
    }

    private ClientResponse getClientResponse(Object st, String path) {
        String masterServer = "http://10.10.253.130:1337/server/";

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("username", "password"));
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
                System.exit(0);
            } else {
                this.id = output.getId();
                Log.info("Server id: " + this.id);

                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
                scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        updateServer();
                    }
                }, 600, 600, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            WindowController.addConsoleMsg("Online server not reachable: " + e.getMessage());
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
                Log.info("Sent alive signal to master server.");
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
    }
}
