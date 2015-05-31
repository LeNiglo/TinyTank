package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 19/03/2015.
 */
public class MessagePlayerDelete extends MessageModel {

    public MessagePlayerDelete() {
    }

    public MessagePlayerDelete(String pseudo, String id) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }
}
