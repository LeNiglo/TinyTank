package com.lefrantguillaume.gestGame.Utils.configs;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkServerConfig {
    private int udpPort;
    private int tcpPort;
    private String address;

    public NetworkServerConfig(int udpPort, int tcpPort, String address) {
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        this.address = address;
    }

    public int getUdpPort(){
        return this.udpPort;
    }
    public int getTcpPort(){
        return this.tcpPort;
    }
    public String getAddress(){
        return this.address;
    }
}
