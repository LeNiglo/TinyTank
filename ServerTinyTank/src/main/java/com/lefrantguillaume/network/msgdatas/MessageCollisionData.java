package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.network.clientmsgs.MessageCollision;

/**
 * Created by Styve on 25/03/2015.
 */
public class MessageCollisionData {
    private Server server;
    private Connection connection;
    private MessageCollision request;

    public MessageCollisionData() {}
    public MessageCollisionData(Server server, Connection connection, MessageCollision request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
    public MessageCollision getRequest() {return request;}
}
