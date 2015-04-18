package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.network.clientmsgs.MessageDelete;

/**
 * Created by Styve on 23/03/2015.
 */
public class MessageDeleteData {
    private Server server;
    private Connection connection;
    private MessageDelete request;

    public MessageDeleteData() {}
    public MessageDeleteData(Server server, Connection connection, MessageDelete request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
    public MessageDelete getRequest() {return request;}
}
