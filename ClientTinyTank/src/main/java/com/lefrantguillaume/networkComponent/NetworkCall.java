package com.lefrantguillaume.networkComponent;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lefrantguillaume.Utils.NetworkConfig;
import com.lefrantguillaume.Utils.NetworkManager;

import java.io.IOException;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkCall {
    Client client = new Client();

    public NetworkCall(NetworkConfig config) {
        NetworkManager.register(client);
        client.start();
        try {
            client.connect(5000, "10.10.253.242", 13333, 13444);

        //a mettre dans une classe
            client.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof NetworkManager.SomeResponse) {
                        NetworkManager.SomeResponse response = (NetworkManager.SomeResponse) object;
                        System.out.println("CLIENT: " + response.text);
                    }
                }
            });

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void call(String value) {
        NetworkManager.SomeRequest request = new NetworkManager.SomeRequest();
        request.text = value;
        client.sendTCP(request);

        System.out.println("request to send: " + value);
    }
}
