package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageModel {
    protected String pseudo;
    protected String id;
    protected boolean playerAction;

    public MessageModel() {}

    public String getPseudo() { return pseudo; }
    public String getId() { return id; }
    public boolean getPlayerAction(){
        return this.playerAction;
    }

    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public void setId(String id) { this.id = id; }
    public void setPlayerAction(boolean playerAction){
        this.playerAction = playerAction;
    }
}