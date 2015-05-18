package com.lefrantguillaume.networkComponent.dataServer;

/**
 * Created by Styve on 24/03/2015.
 */

class AddUserRcv {
    private String name;
    private boolean res;
    private String err;

    AddUserRcv() {
    }

    public String getName() {
        return name;
    }

    public boolean getRes() {
        return res;
    }

    public String getErr() {
        return err;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err);
    }
}

class AddUserSnd {
    private String serverId;
    private String username;

    AddUserSnd() {
    }

    public AddUserSnd(String serverId, String username) {
        this.serverId = serverId;
        this.username = username;
    }

    public String getUsernameId() {
        return username;
    }

    public void setUsernameId(String username) {
        this.username = username;
    }

    public String getServerId() {
        return serverId;
    }

    public void setId(String serverId) {
        this.serverId = serverId;
    }
}
