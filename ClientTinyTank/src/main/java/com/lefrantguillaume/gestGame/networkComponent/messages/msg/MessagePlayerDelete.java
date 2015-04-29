package com.lefrantguillaume.gestGame.networkComponent.messages.msg;

import com.lefrantguillaume.gestGame.networkComponent.messages.MessageModel;

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
