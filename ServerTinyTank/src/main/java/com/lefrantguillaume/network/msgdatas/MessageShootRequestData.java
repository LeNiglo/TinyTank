package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.network.clientmsgs.MessageShoot;

/**
 * Created by Styve on 25/03/2015.
 */
public class MessageShootRequestData {
    private long timestamp;
    private Server server;
    private Connection connection;
    private MessageShoot request;

    public MessageShootRequestData(Connection connection, Server server, MessageShoot request) {
        this.timestamp = System.currentTimeMillis();
        this.connection = connection;
        this.server = server;
        this.request = request;
    }

    public long getTimestamp() {return timestamp;}
    public Server getServer() {return server;}
    public Connection getConnection() {return connection;}
    public MessageShoot getRequest() {return request;}
}
