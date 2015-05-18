package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.master.EnumController;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.utils.ServerConfig;
import javafx.util.Pair;

import java.util.Observable;

/**
 * Created by Styve on 10/03/2015.
 */
public class TinyServer extends Observable {
    private Server server = new Server();

    public TinyServer() {
        NetworkRegister.register(server);
    }

    // FUNCTIONS

    public void doTask(Observable o, Object arg){
        if (arg instanceof MessageModel) {
            this.server.sendToAllTCP(arg);
        } else if (arg instanceof MessageData) {
            Connection connection = ((MessageData) arg).getConnection();
            this.server.sendToTCP(connection.getID(), ((MessageData) arg).getRequest());
        }
    }

    public boolean start() {
        server.start();
        try {
            server.addListener(new Listener() {
                public void connected(Connection connection) {
                    WindowController.addConsoleMsg("Connected: " + connection.getRemoteAddressTCP().getAddress().getHostAddress() + " with Client ID: " + connection.getID());
                }

                public void received(Connection connection, Object object) {
                    if (object instanceof MessageModel) {
                        MessageData msg = new MessageData(connection, (MessageModel) object);
                        TinyServer.this.setChanged();
                        TinyServer.this.notifyObservers(new Pair<>(EnumController.GAME, msg));
                    }
                }

                // TODO: à quoi ça sert ça ?
                public void disconnected(Connection connection) {
                    TinyServer.this.setChanged();
                    TinyServer.this.notifyObservers(new Pair<>(EnumController.UNKNOWN, new MessageData(connection, new MessageDisconnect())));
                }
            });
            server.bind(ServerConfig.tcpPort, ServerConfig.udpPort);
            WindowController.addConsoleMsg("Server listening on port " + ServerConfig.tcpPort + " (tcp) and " + ServerConfig.udpPort + " (udp).");
        } catch (Exception e) {
            WindowController.addConsoleErr(e.getMessage());
            server.close();
            return (false);
        }
        return (true);
    }

    public void stop() {
        server.stop();
        WindowController.addConsoleMsg("Server stopped.");
    }

    // GETTERS
    public Server getServer() {
        return this.server;
    }
}

