package com.lefrantguillaume.network.web;

/**
 * Created by Styve on 10/03/2015.
 */
public class InitServer {
    private String name;
    private boolean res;
    private boolean err;

    public InitServer() {}

    public void setName(String n) { this.name = n; }
    public void setRes(boolean r) { this.res = r; }
    public void setErr(boolean e) { this.err = e; }

    public String getName() { return this.name; }
    public boolean getRes() { return this.res; }
    public boolean getErr() { return this.err; }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res : " + this.res + " Err : " + this.err);
    }
}
