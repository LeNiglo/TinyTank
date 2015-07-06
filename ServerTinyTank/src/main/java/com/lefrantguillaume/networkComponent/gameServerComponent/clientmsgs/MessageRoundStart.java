package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 29/06/2015.
 */
public class MessageRoundStart extends MessageModel {
    private boolean started;

    public MessageRoundStart(){
    }

    public MessageRoundStart(String pseudo, String id, boolean started) {
        this.pseudo = pseudo;
        this.id = id;
        this.started = started;
    }

    public boolean isStarted(){
        return this.started;
    }

}