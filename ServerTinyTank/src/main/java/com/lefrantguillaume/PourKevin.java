package com.lefrantguillaume;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lefrantguillaume.network.Network;

import java.io.IOException;

/**
 * Created by Styve on 11/03/2015.
 */
public class PourKevin {
    public PourKevin() {
        Client client = new Client();
        Network.register(client);
        client.start();
        try {
            client.connect(5000, "127.0.0.1", 13332, 13442);

            Network.SomeRequest request = new Network.SomeRequest();
            request.text = "Here is the request";
            client.sendTCP(request);

            client.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Network.SomeResponse) {
                        Network.SomeResponse response = (Network.SomeResponse) object;
                        System.out.println("CLIENT: " + response.text);
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
