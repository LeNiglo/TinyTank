package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerUpdatePosition extends MessageModel {
    private float x;
    private float y;
    private boolean resetMove;

    public MessagePlayerUpdatePosition() {
    }

    public MessagePlayerUpdatePosition(String pseudo, String id, float x, float y) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
        this.x = x;
        this.y = y;
        this.resetMove = false;
    }

    // GETETRS
    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public boolean isResetMove(){
        return this.resetMove;
    }

    // SETTERS

    public void setResetMove(boolean value){
        this.resetMove = value;
    }
}
