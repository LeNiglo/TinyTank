package com.lefrantguillaume.networkComponent.dataServer;

/**
 * Created by Styve on 11/03/2015.
 */

class UpdateServerRcv {
    private String name;
    private boolean res;
    private String err;

    UpdateServerRcv() {}

    public String getName() { return name; }
    public boolean getRes() { return res; }
    public String getErr() { return err; }

    public void setName(String name) { this.name = name; }
    public void setRes(boolean res) { this.res = res; }
    public void setErr(String err) { this.err = err; }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err);
    }
}

class UpdateServerSnd {
    private String serverId;

    UpdateServerSnd() {}
    public UpdateServerSnd(String serverId) { this.serverId = serverId; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }
}
