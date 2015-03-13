package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.utils.Configuration;

import java.io.IOException;

/**
 * Created by Styve on 10/03/2015.
 */
public class TinyServer {
    private Server server = new Server();

    public TinyServer() {
        Network.register(server);
    }

    public boolean start() {
        if (Configuration.gameName.trim().equals("") || Configuration.tcpPort == 0 || Configuration.maxAllowedPlayers == 0 ||
                Configuration.maxAllowedPing <= 20 || Configuration.ptsLimit == 0 || Configuration.timeLimit == 0)
            return false;
        server.start();
        try {
            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Network.MessageMove) {
                        isMessageMove(connection, (Network.MessageMove) object);
                    } else if (object instanceof  Network.MessageShoot) {
                        isMessageShoot(connection, (Network.MessageShoot) object);
                    }
                }
            });
            server.bind(Configuration.tcpPort, Configuration.udpPort);
            WindowController.addConsoleMsg("Server listening on port " + Configuration.tcpPort + " (tcp) and " + Configuration.udpPort + " (udp).");
            return (true);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            server.close();
        }
        return (false);
    }

    public void stop() {
        server.stop();
    }

    private void isMessageMove(Connection connection, Network.MessageMove request) {
        System.out.println("direction recue: " + request.getDirection() + " // move : " + (request.getMove() ? "true" : "false"));
        server.sendToAllTCP(request);
    }

    private void isMessageShoot(Connection connection, Network.MessageShoot request) {
        System.out.println("tir recu: " + request.getValueKeyPressed());
        server.sendToAllTCP(request);
    }
}
