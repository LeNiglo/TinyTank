package com.lefrantguillaume.components.networkComponent.networkGame.messages;

/**
 * Created by andres_k on 13/03/2015.
 */
public class MessageModel {
    protected String pseudo;
    protected String id;
    protected boolean playerAction;

    // GETTERS
    public String getPseudo() {
        return this.pseudo;
    }

    public String getId() {
        return this.id;
    }

    public boolean getPlayerAction(){
        return this.playerAction;
    }

    // SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPlayerAction(boolean playerAction){
        this.playerAction = playerAction;
    }
}
