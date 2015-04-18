package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.util.List;

/**
 * Created by Styve on 21/03/2015.
 */
public class MessageConnectData {
    private Server server;
    private Connection connection;

    public MessageConnectData() {}
    public MessageConnectData(Server server, Connection connection) {
        this.server = server;
        this.connection = connection;
    }

    public Server getServer() {return server;}
    public Connection getConnection() {return connection;}
    public void setServer(Server server) {this.server = server;}
    public void setConnection(Connection connection) {this.connection = connection;}
}
