package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageMove extends MessageModel {
    private int direction;
    private boolean move;
    private float playerX;
    private float playerY;

    public MessageMove(){}

    public MessageMove(String pseudo, String id, int direction, boolean move, float playerX, float playerY){
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.direction = direction;
        this.move = move;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    // GETTERS

    public int getDirection() {
        return direction;
    }

    public float getPlayerX(){
        return this.playerX;
    }

    public float getPlayerY(){
        return this.playerY;
    }

    // SETTERS

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isMove() {
        return move;
    }
}
