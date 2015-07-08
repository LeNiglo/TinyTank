package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 08/07/2015.
 */
public class MessageDeleteObject extends MessageModel{
    private String idObject;

    public MessageDeleteObject(){
    }

    public MessageDeleteObject(String pseudo, String id, String idObject){
        this.pseudo = pseudo;
        this.id = id;
        this.idObject = idObject;
    }

    // GETTERS
    public String getIdObject(){
        return this.idObject;
    }
}
