package com.lefrantguillaume.networkComponent.gameServer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.master.EnumController;
import com.lefrantguillaume.networkComponent.gameServer.clientmsgs.*;
import com.lefrantguillaume.utils.ServerConfig;
import javafx.util.Pair;

import java.util.Observable;

/**
 * Created by Styve on 10/03/2015.
 */
public class GameServer extends Observable {
    private Server server = new Server();

    public GameServer() {
        NetworkRegister.register(server);
    }

    // FUNCTIONS

    public void doTask(Observable o, Object arg) {
        if (arg instanceof Request) {
            Connection connection = ((Request) arg).getConnection();
            if (connection == null) {
                this.server.sendToAllTCP(arg);
            } else {
                this.server.sendToTCP(connection.getID(), ((Request) arg).getRequest());
            }
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
                        Request msg = new Request(connection, (MessageModel) object);
                        GameServer.this.setChanged();
                        GameServer.this.notifyObservers(new Pair<>(EnumController.GAME, msg));
                    }
                }

                public void disconnected(Connection connection) {
                    GameServer.this.setChanged();
                    GameServer.this.notifyObservers(new Pair<>(EnumController.MASTER_SERVER, new Request(connection, new MessageDisconnect())));
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

