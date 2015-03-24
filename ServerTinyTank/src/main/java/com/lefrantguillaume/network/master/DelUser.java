package com.lefrantguillaume.network.master;

/**
 * Created by Styve on 24/03/2015.
 */

class DelUserRcv {
    private String name;
    private boolean res;
    private String err;

    DelUserRcv() {}

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

class DelUserSnd {
    private String username;
    private String serverId;

    DelUserSnd() {}
    DelUserSnd(String serverId, String username) {
        this.serverId = serverId;
        this.username = username;
    }

    public String getUsernameId() { return username; }
    public void setUsernameId(String username) { this.username = username; }
    public String getServerId() {return serverId;}
    public void setServerId(String serverId) {this.serverId = serverId;}
}