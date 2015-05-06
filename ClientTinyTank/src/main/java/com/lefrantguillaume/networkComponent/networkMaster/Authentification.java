package com.lefrantguillaume.networkComponent.networkMaster;

/**
 * Created by Styve on 10/03/2015.
 */

class AuthRcv {
    private String name;
    private String err;
    private String id;
    //TODO, check what is the return value of res. ( should be JsonObject of the user if succes or null if error )
    private boolean res;

    public AuthRcv() {
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

class AuthSnd {
    private String login;
    private String password;

    public AuthSnd(String l, String p) {
        this.login = l;
        this.password = p;
    }

    public AuthSnd() {}
}
