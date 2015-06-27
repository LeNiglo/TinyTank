package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 27/06/2015.
 */
public class MessageRoundKill extends MessageModel {
    private String killer;
    private String target;
    private String killerTeam;
    private String targetTeam;

    public MessageRoundKill(){
    }

    // GETTERS

    public String getKiller(){
        return this.killer;
    }

    public String getTarget(){
        return this.target;
    }

    public String getKillerTeam() {
        return this.killerTeam;
    }

    public String getTargetTeam() {
        return this.targetTeam;
    }
}
