package com.lefrantguillaume.components.networkComponent.networkData;


/**
 * Created by Styve on 10/03/2015.
 */

class AuthRcv {
    private String name;
    private String err;
    private String id;
    //TODO, check what is the return value of res. ( should be JsonObject of the user if succes or null if error )
    private String res;

    public AuthRcv() {
    }

    public void setName(String name) { this.name = name; }
    public void setErr(String err) { this.err = err; }
    public void setId(String id) { this.id = id; }
    public void setRes(String res) { this.res = res; }

    public String getName() { return name; }
    public String getErr() { return err; }
    public String getRes() { return res; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err + " / id: " + this.id);
    }

}

class AuthSnd {
    private String login;
    private String password;
    private String secret;

    public AuthSnd(String l, String p, String s) {
        this.login = l;
        this.password = p;
        this.secret = s;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
