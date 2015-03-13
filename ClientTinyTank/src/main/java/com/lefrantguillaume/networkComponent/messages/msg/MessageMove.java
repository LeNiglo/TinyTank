package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageMove extends MessageModel {
    private int direction;
    private boolean move;

    public MessageMove(){};
    public MessageMove(String pseudo, int id, int direction, boolean move){
        this.pseudo = pseudo;
        this.direction = direction;
        this.id = id;
        this.move = move;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isMove() {
        return move;
    }
}
