package com.lefrantguillaume.gameComponent;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerState {
    private float x;
    private float y;
    private boolean move;
    private EnumDirection direction;
    private int userId;

    public PlayerState(float x, float y, int userId) {
        this.x = x;
        this.y = y;
        this.move = false;
        this.direction = EnumDirection.DOWN;
    }

    public int getUserId() {
        return this.userId;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public EnumDirection getDirection() {
        return direction;
    }

    public void setDirection(EnumDirection direction) {
        this.direction = direction;
    }
}
