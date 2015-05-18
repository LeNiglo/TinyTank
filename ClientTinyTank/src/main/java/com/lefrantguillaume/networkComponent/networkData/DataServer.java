package com.lefrantguillaume.networkComponent.networkData;

import com.esotericsoftware.minlog.Log;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Created by Styve on 10/03/2015.
 */
public class DataServer {
    private String id = null;

    public DataServer() {
    }

    private static ClientResponse getClientResponse(Object st, String path) {
        String masterServer = "http://127.0.0.1:1337/client/";

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv", "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo"));
        WebResource webResource = client.resource(masterServer + path);
        ClientResponse response = webResource.accept("application/json")
                .type("application/json")
                .post(ClientResponse.class, st);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    public static Object authentification(String u, String p) {
        try {
            AuthSnd st = new AuthSnd(u, p, "TheSecretStringIsMuchStrongerThanOneMillionOfTanks");

            ClientResponse response = DataServer.getClientResponse(st, "login");
            AuthRcv output = response.getEntity(AuthRcv.class);
            if (output.getRes() == null) {
                Log.error("DataServer server error: " + output.getErr());
                // TODO not exit 0 but display error message then quit because you are screwed
                System.exit(0);
            } else {
               return output.getRes();
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
