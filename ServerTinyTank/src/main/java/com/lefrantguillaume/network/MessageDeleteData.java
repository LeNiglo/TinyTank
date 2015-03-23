package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

/**
 * Created by Styve on 23/03/2015.
 */
public class MessageDeleteData {
    private Server server;
    private Connection connection;
    private Network.MessageDelete request;

    public MessageDeleteData() {}
    public MessageDeleteData(Server server, Connection connection, Network.MessageDelete request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
    public Network.MessageDelete getRequest() {return request;}
}
