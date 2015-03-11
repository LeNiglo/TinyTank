package com.lefrantguillaume.network.master;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.network.Network;
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
    private String masterServer = "http://username:password@10.10.252.222:1337/server";
    private String gameName;

    public Master() {
        this.gameName = "Ma partie de jeu";
    }

    public Master(String gameName) {
        this.gameName = gameName;
    }

    private Client getClient() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("username", "password"));
        return client;
    }

    public void initServer() {
        try {
            InitServerSnd st = new InitServerSnd(Network.getIp(), this.gameName, Network.tcpPort, Network.udpPort);
            Client client = this.getClient();
            WebResource webResource = client.resource(this.masterServer + "/init_server");
            ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, st);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            InitServerRcv output = response.getEntity(InitServerRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
                System.exit(0);
            }
            this.id = output.getId();
            Log.info("Server id: " + this.id);

            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    updateServer();
                }
            }, 600, 600, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
    }

    public void updateServer() {
        try {
            UpdateServerSnd st = new UpdateServerSnd(this.id);
            Client client = this.getClient();
            WebResource webResource = client.resource(this.masterServer + "/update_last_active");
            ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, st);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
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
