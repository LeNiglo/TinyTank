package com.lefrantguillaume.components.networkComponent.networkData;


/**
 * Created by Styve on 10/03/2015.
 */

class ServerListRcv {
    private String name;
    private String err;
    private String res;

    public ServerListRcv() {
    }

    public void setName(String name) { this.name = name; }
    public void setErr(String err) { this.err = err; }
    public void setRes(String res) { this.res = res; }

    public String getName() { return name; }
    public String getErr() { return err; }
    public String getRes() { return res; }

}