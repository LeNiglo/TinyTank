package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 29/06/2015.
 */
public class MessageRoundState {
    private boolean started;

    public MessageRoundState(){
    }

    public MessageRoundState(boolean started){
        this.started = started;
    }

    public boolean isStarted(){
        return this.started;
    }
}
