package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageDisconnect extends MessageModel {
    private boolean sucess;

    public MessageDisconnect(){};
    public MessageDisconnect(String pseudo, String id) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }
}
