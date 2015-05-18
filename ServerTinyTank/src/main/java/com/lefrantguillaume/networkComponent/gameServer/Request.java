package com.lefrantguillaume.networkComponent.gameServer;

import com.esotericsoftware.kryonet.Connection;
import com.lefrantguillaume.networkComponent.gameServer.clientmsgs.MessageModel;

/**
 * Created by Styve on 07/05/2015.
 */
public class Request {
    private Connection connection;
    private MessageModel request;

    public Request(Connection connection, MessageModel request) {
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public MessageModel getRequest() {
        return this.request;
    }
}
