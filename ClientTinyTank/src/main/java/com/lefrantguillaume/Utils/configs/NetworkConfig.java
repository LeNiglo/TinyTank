package com.lefrantguillaume.Utils.configs;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkConfig {
    private int udpPort;
    private int tcpPort;
    private String address;

    public NetworkConfig() {
        this.udpPort = 13444;
        this.tcpPort = 13333;
        this.address = "localhost";
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
