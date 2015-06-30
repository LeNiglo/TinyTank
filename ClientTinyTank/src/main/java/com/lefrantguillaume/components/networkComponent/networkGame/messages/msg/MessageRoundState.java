package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 29/06/2015.
 */
public class MessageRoundState extends MessageModel {
    private boolean started;

    MessageRoundState(){
    }

    public boolean isStarted(){
        return this.started;
    }
}
