package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.network.clientmsgs.MessagePlayerNew;

/**
 * Created by Styve on 23/03/2015.
 */
public class MessagePlayerNewData {
    private Server server;
    private Connection connection;
    private com.lefrantguillaume.network.clientmsgs.MessagePlayerNew request;

    public MessagePlayerNewData() {}
    public MessagePlayerNewData(Server server, Connection connection, com.lefrantguillaume.network.clientmsgs.MessagePlayerNew request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
    public com.lefrantguillaume.network.clientmsgs.MessagePlayerNew getRequest() {return request;}
}
