package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 19/03/2015.
 */
public class MessagePlayerDelete extends MessageModel {
    MessagePlayerDelete(String pseudo, String id){
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }
}
