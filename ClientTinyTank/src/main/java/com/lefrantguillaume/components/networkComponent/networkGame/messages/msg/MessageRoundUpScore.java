package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 27/06/2015.
 */
public class MessageRoundUpScore extends MessageModel {
    private String teamId;
    private EnumGameObject object;
    private int upgrade;

    public MessageRoundUpScore(){
    }

    public MessageRoundUpScore(String pseudo, String id, String teamId, EnumGameObject object, int upgrade){
        this.pseudo = pseudo;
        this.id = id;
        this.teamId = teamId;
        this.object = object;
        this.upgrade = upgrade;
    }

    // GETTERS
    public String getTeamId() {
        return this.teamId;
    }

    public EnumGameObject getObject() {
        return this.object;
    }

    public int getUpgrade() {
        return this.upgrade;
    }
}
