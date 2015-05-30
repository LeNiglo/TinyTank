package com.lefrantguillaume.networkComponent.networkGame;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lefrantguillaume.Utils.configs.NetworkServerConfig;
import com.lefrantguillaume.networkComponent.networkGame.messages.MessageModel;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkCall extends Observable {
    Client client = new Client();

    public NetworkCall(NetworkServerConfig config) {
        NetworkRegister.register(client);
        client.start();
        this.connect(config);
    }

    public boolean connect(NetworkServerConfig config){
        try {
            client.connect(5000, config.getAddress(), config.getTcpPort(), config.getUdpPort());
            client.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof MessageModel) {
                        MessageModel response = (MessageModel) object;
                        setChanged();
                        notifyObservers(response);
                    }
                }
            });
            return true;

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public void disconnect(){
        client.close();
    }

    public void call(MessageModel request) {
        client.sendTCP(request);
    }
}
