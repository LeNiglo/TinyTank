package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerUpdatePosition extends MessageModel {
    private float x;
    private float y;

    public MessagePlayerUpdatePosition() {
    }

    public MessagePlayerUpdatePosition(String pseudo, String id, float x, float y) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }
}
