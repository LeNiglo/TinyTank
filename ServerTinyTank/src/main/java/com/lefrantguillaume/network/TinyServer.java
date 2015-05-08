package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.network.msgdatas.*;
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
                    if (object instanceof MessageModel) {
                        MessageData msg = new MessageData(server, connection, (MessageModel) object);
                        TinyServer.this.setChanged();
                        TinyServer.this.notifyObservers(msg);
                    } else {
                        TinyServer.this.setChanged();
                        TinyServer.this.notifyObservers(object);
                    }
                }

                public void disconnected(Connection connection) {
                    TinyServer.this.setChanged();
                    TinyServer.this.notifyObservers(new MessageData(server, connection, new MessageDisconnect()));
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
}

