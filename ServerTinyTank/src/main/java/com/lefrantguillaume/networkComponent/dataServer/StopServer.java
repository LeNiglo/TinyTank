package com.lefrantguillaume.networkComponent.dataServer;

/**
 * Created by Styve on 24/03/2015.
 */

class StopServerRcv {
    private String name;
    private boolean res;
    private String err;

    StopServerRcv() {}

    public String getName() { return name; }
    public boolean getRes() { return res; }
    public String getErr() { return err; }

    public void setName(String name) { this.name = name;}
    public void setRes(boolean res) { this.res = res; }
    public void setErr(String err) { this.err = err; }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err);
    }
}

class StopServerSnd {
    private String serverId;

    StopServerSnd() {}
    public StopServerSnd(String serverId) { this.serverId = serverId; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }
}
