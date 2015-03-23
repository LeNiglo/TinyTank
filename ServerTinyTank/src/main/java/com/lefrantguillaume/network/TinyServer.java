package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.utils.ServerConfig;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by Styve on 10/03/2015.
 */
public class TinyServer extends Observable {
    private Server server = new Server();

    public TinyServer() {
        Network.register(server);
    }

    public boolean start() {
        server.start();
        try {
            server.addListener(new Listener() {
                public void connected(Connection connection) {
                    WindowController.addConsoleMsg("Connected: " + connection.getRemoteAddressTCP().getHostName() + " with Client ID: " + connection.getID());
                }

                public void received(Connection connection, Object object) {
                    if (object instanceof Network.MessageMove) {
                        isMessageMove(connection, (Network.MessageMove) object);
                    } else if (object instanceof Network.MessageShoot) {
                        isMessageShoot(connection, (Network.MessageShoot) object);
                    } else if (object instanceof Network.MessageConnect) {
                        isMessageConnect(connection, (Network.MessageConnect) object);
                    } else if (object instanceof Network.MessageNeedMap) {
                        isMessageNeedMap(connection, (Network.MessageNeedMap) object);
                    } else if (object instanceof Network.MessageSpell) {
                        isMessageSpell(connection, (Network.MessageSpell) object);
                    } else if (object instanceof Network.MessageChangeTeam) {
                        isMessageChangeTeam(connection, (Network.MessageChangeTeam) object);
                    } else if (object instanceof Network.MessageDelete) {
                        isMessageDelete(connection, (Network.MessageDelete) object);
                    } else if (object instanceof Network.MessagePlayerNew) {
                        isMessagePlayerNew(connection, (Network.MessagePlayerNew) object);
                    } else if (object instanceof Network.MessagePlayerNew) {
                        isMessageTankChoice(connection, (Network.MessagePlayerNew) object);
                    }
                }

                public void disconnected(Connection connection) {
                    WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
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
        server.stop();
        WindowController.addConsoleMsg("Server stopped.");
    }

    public Server getServer() {
        return this.server;
    }

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
        MessageConnectData mcd = new MessageConnectData(server, connection);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mcd);
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

    private void isMessageNeedMap(final Connection connection, Network.MessageNeedMap request) {
        System.out.println("Il a besoin de la map");
        MessageDownloadData mdd = new MessageDownloadData(server, connection);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mdd);
    }

    private void isMessageTankChoice(Connection connection, Network.MessagePlayerNew request) {
        Network.EnumTanks tankId = request.getEnumTanks();
        String tank = (tankId == Network.EnumTanks.RUSHER ? "Rusher" : (tankId == Network.EnumTanks.SNIPER ? "Sniper" : (tankId == Network.EnumTanks.TIGER ? "Tiger" : "NULL")));
        System.out.println(request.getPseudo() + " a choisi le tank: " + tank);
        MessageTankData mtd = new MessageTankData(server, connection, request);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mtd);
    }
}

