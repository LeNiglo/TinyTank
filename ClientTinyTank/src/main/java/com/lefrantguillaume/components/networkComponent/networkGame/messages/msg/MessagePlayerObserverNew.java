package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerObserverNew extends MessageModel {
    private String teamId;

    public MessagePlayerObserverNew() {
    }

    public MessagePlayerObserverNew(String pseudo, String id) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }

    public String getTeamId(){
        return this.teamId;
    }
}
