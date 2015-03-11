package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

/**
 * Created by Styve on 10/03/2015.
 */
public class TinyServer {
    private int tcpPort;
    private int udpPort;

    public TinyServer() {
        this.tcpPort = Network.tcpPort;
        this.udpPort = Network.udpPort;
    }

    public TinyServer(int tcpPort, int udpPort) {
        Network.tcpPort = tcpPort;
        Network.udpPort = udpPort;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public void start() {
        Server server = new Server();
        Network.register(server);
        server.start();
        try {
            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Network.SomeRequest) {
                        isSomeRequest(connection, (Network.SomeRequest) object);
                    }
                }
            });
            server.bind(this.tcpPort, this.udpPort);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            server.close();
        }
    }

    private void isSomeRequest(Connection connection, Network.SomeRequest request) {
        System.out.println("SERVER: " + request.text);

        Network.SomeResponse response = new Network.SomeResponse();
        response.text = "Thanks";
        connection.sendTCP(response);
    }
}
