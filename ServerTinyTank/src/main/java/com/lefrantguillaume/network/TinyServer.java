package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.game.gameobjects.tanks.types.EnumTanks;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.network.msgdatas.*;
import com.lefrantguillaume.network.msgdatas.MessagePlayerNewData;
import com.lefrantguillaume.utils.ServerConfig;

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
                    WindowController.addConsoleMsg("Connected: " + connection.getRemoteAddressTCP().getAddress().getHostAddress() + " with Client ID: " + connection.getID());
                }

                public void received(Connection connection, Object object) {
                    if (object instanceof MessageMove) {
                        isMessageMove(connection, (MessageMove) object);
                    } else if (object instanceof MessageShoot) {
                        isMessageShoot(connection, (MessageShoot) object);
                    } else if (object instanceof MessageConnect) {
                        isMessageConnect(connection, (MessageConnect) object);
                    } else if (object instanceof MessageNeedMap) {
                        isMessageNeedMap(connection, (MessageNeedMap) object);
                    } else if (object instanceof MessageSpell) {
                        isMessageSpell(connection, (MessageSpell) object);
                    } else if (object instanceof MessageChangeTeam) {
                        isMessageChangeTeam(connection, (MessageChangeTeam) object);
                    } else if (object instanceof MessageDelete) {
                        isMessageDelete(connection, (MessageDelete) object);
                    } else if (object instanceof com.lefrantguillaume.network.clientmsgs.MessagePlayerNew) {
                        isMessagePlayerNew(connection, (com.lefrantguillaume.network.clientmsgs.MessagePlayerNew) object);
                    } else if (object instanceof MessagePlayerUpdatePosition) {
                        isMessagePlayerUpdatePosition(connection, (MessagePlayerUpdatePosition) object);
                    } else if (object instanceof MessageCollision) {
                        isMessageCollision(connection, (MessageCollision) object);
                    } else if (object instanceof MessagePutObject) {
                        isMessagePutObject(connection, (MessagePutObject) object);
                    }
                }

                public void disconnected(Connection connection) {
                    WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
                    MessageDisconnectData mdd = new MessageDisconnectData(connection);
                    TinyServer.this.setChanged();
                    TinyServer.this.notifyObservers(mdd);
                }
            });
            server.bind(ServerConfig.tcpPort, ServerConfig.udpPort);
            WindowController.addConsoleMsg("Server listening on port " + ServerConfig.tcpPort + " (tcp) and " + ServerConfig.udpPort + " (udp).");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
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

    private void isMessageMove(Connection connection, MessageMove request) {
        System.out.println("direction recue: " + request.getDirection() + " // move : " + (request.getMove() ? "true" : "false"));
        server.sendToAllTCP(request);
    }

    private void isMessageShoot(Connection connection, MessageShoot request) {
        MessageShootRequestData mrd = new MessageShootRequestData(connection, server, request);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mrd);
    }

    private void isMessageConnect(Connection connection, MessageConnect request) {
        System.out.println("Nouvelle connection: " + request.getPseudo() + " est sous l'id " + request.getId());
        MessageConnectData mcd = new MessageConnectData(server, connection);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mcd);
    }

    private void isMessageSpell(Connection connection, MessageSpell request) {
        System.out.println("sort de " + request.getPseudo() + ": " + request.getX() + ", " + request.getY());
        server.sendToAllTCP(request);
    }

    private void isMessageChangeTeam(Connection connection, MessageChangeTeam request) {
        System.out.println(request.getPseudo() + " change de team.");
        server.sendToAllTCP(request);
    }

    private void isMessageDelete(Connection connection, MessageDelete request) {
        System.out.println(request.getPseudo() + " a envoyé un message DELETE");
        MessageDeleteData mdd = new MessageDeleteData(server, connection, request);
        server.sendToAllTCP(request);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mdd);
    }

    private void isMessageNeedMap(final Connection connection, MessageNeedMap request) {
        System.out.println("Il a besoin de la map");
        MessageDownloadData mdd = new MessageDownloadData(server, connection);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mdd);
    }

    private void isMessagePlayerNew(Connection connection, com.lefrantguillaume.network.clientmsgs.MessagePlayerNew request) {
        System.out.println("Nouveau joueur: " + request.getPseudo() + " with :" + request.getEnumTanks().getValue());
        MessagePlayerNewData mtd = new MessagePlayerNewData(server, connection, request);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mtd);
    }

    private void isMessagePlayerUpdatePosition(Connection connection, MessagePlayerUpdatePosition request) {
        System.out.println("Update: " + request.getX() + " / " + request.getY());
        server.sendToAllExceptTCP(connection.getID(), request);
    }

    private void isMessageCollision(Connection connection, MessageCollision request) {
        MessageCollisionData mcd = new MessageCollisionData(server, connection, request);
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(mcd);
    }

    private void isMessagePutObject(Connection connection, MessagePutObject request) {
        TinyServer.this.setChanged();
        TinyServer.this.notifyObservers(request);
    }
}

