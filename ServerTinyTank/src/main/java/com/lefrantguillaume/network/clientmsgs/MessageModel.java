package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageModel {
    protected String pseudo;
    protected String id;

    public MessageModel() {}

    public String getPseudo() { return pseudo; }
    public String getId() { return id; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public void setId(String id) { this.id = id; }
}