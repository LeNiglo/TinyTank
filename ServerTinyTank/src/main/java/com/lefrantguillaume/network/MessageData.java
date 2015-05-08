package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.network.clientmsgs.MessageModel;

/**
 * Created by Styve on 07/05/2015.
 */
public class MessageData {
    private Server server;
    private Connection connection;
    private MessageModel request;

    public MessageData(Server server, Connection connection, MessageModel request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Server getServer() {
        return server;
    }

    public Connection getConnection() {
        return connection;
    }

    public MessageModel getRequest() {
        return request;
    }
}
