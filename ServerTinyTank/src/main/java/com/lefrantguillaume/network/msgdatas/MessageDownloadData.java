package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class MessageDownloadData {
    private Server server;
    private Connection connection;

    public MessageDownloadData() {}
    public MessageDownloadData(Server server, Connection connection) {this.server = server; this.connection = connection;}

    public Connection getConnection() {return connection;}
    public Server getServer() {return server;}
}
