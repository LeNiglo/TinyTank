package com.lefrantguillaume.networkComponent.gameServerComponent;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.master.EnumTargetTask;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageDisconnect;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
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
            MessageModel message = ((Request) arg).getRequest();
            Connection connection = ((Request) arg).getConnection();
            if (connection == null) {
                this.server.sendToAllTCP(message);
            } else {
                this.server.sendToTCP(connection.getID(), message);
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
                        Request task = new Request(connection, (MessageModel) object);
                        GameServer.this.setChanged();
                        GameServer.this.notifyObservers(new Pair<>(EnumTargetTask.GAME, task));
                    }
                }

                public void disconnected(Connection connection) {
                    GameServer.this.setChanged();
                    GameServer.this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_SERVER, new Request(connection, new MessageDisconnect())));
                }
            });
            server.bind(ServerConfig.tcpPort, ServerConfig.udpPort);
            WindowController.addConsoleMsg("Server listening on port " + ServerConfig.tcpPort + " (tcp) and " + ServerConfig.udpPort + " (udp).");
        } catch (Exception e) {
            WindowController.addConsoleErr(e.getMessage());
            e.printStackTrace();
            server.close();
            return (false);
        }
        return (true);
    }

    public void stop() {
        server.stop();
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_CONTROLLER, "stop game"));
        WindowController.addConsoleMsg("Server stopped.");
    }

    // GETTERS
    public Server getServer() {
        return this.server;
    }
}

