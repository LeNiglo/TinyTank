package com.lefrantguillaume.networkComponent.networkMaster;

import com.esotericsoftware.minlog.Log;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Styve on 10/03/2015.
 */
public class Master {
    private String id = null;

    public Master() {
    }

    private ClientResponse getClientResponse(Object st, String path) {
        String masterServer = "http://127.0.0.1:1337/client/";

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

    public boolean authentification() {
        try {
            // TODO user login or email as first param and password as second
            AuthSnd st = new AuthSnd("Draym", "superDraym", "TheSecretStringIsMuchStrongerThanOneMillionOfTanks");

            ClientResponse response = this.getClientResponse(st, "login");
            AuthRcv output = response.getEntity(AuthRcv.class);
            if (!output.getRes()) {
                Log.error("Master server error: " + output.getErr());
                //TODO no exit 0 but display error message then quit
                System.exit(0);
            } else {
                // HERE IS WHERE YOU GET THE _ID OF THE USER !!
                /*
                this.id = output.getId();
                Log.info("Player id: " + this.id);
                */
            }
        } catch (ClientHandlerException e) {
            // TODO on the client, display a fatal error if no auth to master
            // WindowController.addConsoleMsg("Online server not reachable: " + e.getCause().getMessage());
            return false;
        }
        return true;
    }

}
