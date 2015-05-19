package com.lefrantguillaume.networkComponent.dataServer;

/**
 * Created by Styve on 10/03/2015.
 */

class InitServerRcv {
    private String name;
    private String err;
    private String id;
    private boolean res;

    public InitServerRcv() {
    }

    public void setName(String name) { this.name = name; }
    public void setErr(String err) { this.err = err; }
    public void setRes(boolean res) { this.res = res; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getErr() { return err; }
    public boolean getRes() { return res; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err + " / id: " + this.id);
    }
}

class InitServerSnd {
    private String gameName;
    private int tcpPort;
    private int udpPort;

    public InitServerSnd(String gameName, int tcpPort, int udpPort) {
        this.gameName = gameName;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public String getGameName() { return gameName; }
    public int getTcpPort() { return tcpPort; }
    public int getUdpPort() { return udpPort; }

    public void setGameName(String gameName) { this.gameName = gameName; }
    public void setTcpPort(int tcpPort) { this.tcpPort = tcpPort; }
    public void setUdpPort(int udpPort) { this.udpPort = udpPort; }
}
