package com.lefrantguillaume.network.web;

import com.lefrantguillaume.network.Network;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Created by Styve on 10/03/2015.
 */
public class Web {
    public Web() {
        try {
            Network.SomeRequest st = new Network.SomeRequest();
            st.text = "salut";
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(
                    JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(clientConfig);
            WebResource webResource = client.resource("http://10.10.252.222:6668/servers/init_server");
            ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, st);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            InitServer output = response.getEntity(InitServer.class);
            System.out.println("Server response .... \n");
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
