package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 20/06/2015.
 */
public class MessageChat extends MessageModel {
    private boolean all;
    private String message;

    MessageChat(){
    }

    MessageChat(String pseudo, String id, boolean all, String message){
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
        this.all = all;
        this.message = message;
    }

    // GETTERS
    public boolean isAll() {
        return this.all;
    }

    public String getMessage() {
        return this.message;
    }
}
