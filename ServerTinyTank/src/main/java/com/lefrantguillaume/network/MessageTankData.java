package com.lefrantguillaume.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

/**
 * Created by Styve on 23/03/2015.
 */
public class MessageTankData {
    private Server server;
    private Connection connection;
    private Network.MessagePlayerNew request;

    public MessageTankData() {}
    public MessageTankData(Server server, Connection connection, Network.MessagePlayerNew request) {
        this.server = server;
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
    public Network.MessagePlayerNew getRequest() {return request;}
}
