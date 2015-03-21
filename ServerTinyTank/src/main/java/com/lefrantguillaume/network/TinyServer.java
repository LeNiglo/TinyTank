package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.utils.ServerConfig;

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
        server.start();
        try {
            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Network.MessageMove) {
                        isMessageMove(connection, (Network.MessageMove) object);
                    } else if (object instanceof  Network.MessageShoot) {
                        isMessageShoot(connection, (Network.MessageShoot) object);
                    } else if (object instanceof  Network.MessageConnect) {
                        isMessageConnect(connection, (Network.MessageConnect) object);
                    } else if (object instanceof  Network.MessageHasMap) {
                        isMessageHasMap(connection, (Network.MessageHasMap) object);
                    } else if (object instanceof  Network.MessageSpell) {
                        isMessageSpell(connection, (Network.MessageSpell) object);
                    } else if (object instanceof  Network.MessageChangeTeam) {
                        isMessageChangeTeam(connection, (Network.MessageChangeTeam) object);
                    } else if (object instanceof  Network.MessageDelete) {
                        isMessageDelete(connection, (Network.MessageDelete) object);
                    } else if (object instanceof  Network.MessagePlayerNew) {
                        isMessagePlayerNew(connection, (Network.MessagePlayerNew) object);
                    } else if (object instanceof  Network.MessagePlayerUpdate) {
                        isMessagePlayerUpdate(connection, (Network.MessagePlayerUpdate) object);
                    }
                }
            });
            server.bind(ServerConfig.tcpPort, ServerConfig.udpPort);
            WindowController.addConsoleMsg("Server listening on port " + ServerConfig.tcpPort + " (tcp) and " + ServerConfig.udpPort + " (udp).");
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            server.close();
            return (false);
        }
        return (true);
    }

    public void stop() {
        WindowController.addConsoleMsg("Server stopped.");
        server.stop();
    }

    public Server getServer() { return this.server; }

    private void isMessageMove(Connection connection, Network.MessageMove request) {
        System.out.println("direction recue: " + request.getDirection() + " // move : " + (request.getMove() ? "true" : "false"));
        server.sendToAllTCP(request);
    }

    private void isMessageShoot(Connection connection, Network.MessageShoot request) {
        System.out.println("tir recu: " + request.getValueKeyPressed() + " / angle: " + request.getAngle());
        server.sendToAllTCP(request);
    }

    private void isMessageConnect(Connection connection, Network.MessageConnect request) {
        System.out.println("Nouvelle connection: " + request.getPseudo() + " est sous l'id " + request.getId());
        Network.MessageConnect response = new Network.MessageConnect("jolie_map.jpg", "0000");
        server.sendToTCP(connection.getID(), response);
    }

    private void isMessageSpell(Connection connection, Network.MessageSpell request) {
        System.out.println("sort de " + request.getPseudo() + ": " + request.getX() + ", " + request.getY());
        server.sendToAllTCP(request);
    }

    private void isMessageChangeTeam(Connection connection, Network.MessageChangeTeam request) {
        System.out.println(request.getPseudo() + " change de team.");
        server.sendToAllTCP(request);
    }

    private void isMessageDelete(Connection connection, Network.MessageDelete request) {
        System.out.println(request.getPseudo() + " a envoyé un message DELETE");
        server.sendToAllTCP(request);
    }

    private void isMessagePlayerNew(Connection connection, Network.MessagePlayerNew request) {
        System.out.println("Nouveau joueur: " + request.getPseudo());
        server.sendToAllTCP(request);
    }

    private void isMessagePlayerUpdate(Connection connection, Network.MessagePlayerUpdate request) {
        System.out.println("Update du joueur: " + request.getPseudo());
        server.sendToAllTCP(request);
    }

    private void isMessageHasMap(final Connection connection, Network.MessageHasMap request) {
        System.out.println((request.isValue() ? "Le client a la map" : "Le client n'a pas la map"));
        if (!request.isValue()) {
            new Thread("upload") {
                public void run() {
                    Network.MessageDownloadAnswer response = new Network.MessageDownloadAnswer("jolie_map.jpg", 151544);
                    server.sendToTCP(connection.getID(), response);
                    try {
                        new SendFile("jolie_map.jpg");
                    } catch (Exception e) {
                        System.out.println("Cannot send file: " + e.getMessage());
                    }
                }
            }.start();
        }
    }
}
