package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.network.clientmsgs.MessagePlayerNew;

/**
 * Created by Styve on 23/03/2015.
 */
public class MessageTankData {
    private Server server;
    private Connection connection;
    private MessagePlayerNew request;

    public MessageTankData() {}
    public MessageTankData(Server server, Connection connection, MessagePlayerNew request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
    public MessagePlayerNew getRequest() {return request;}
}
