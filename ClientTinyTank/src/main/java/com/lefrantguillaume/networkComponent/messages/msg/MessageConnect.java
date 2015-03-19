package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageConnect extends MessageModel {
    public MessageConnect() {
    }

    public MessageConnect(String pseudo) {
        this.pseudo = pseudo;
        this.id = -1;
    }
}