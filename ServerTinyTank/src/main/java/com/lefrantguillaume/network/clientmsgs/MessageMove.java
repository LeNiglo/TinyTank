package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageMove extends MessageModel {
    private int direction;
    private boolean move;

    public MessageMove() {}

    public int getDirection() { return direction; }
    public boolean getMove() { return move; }
    public void setDirection(int direction) { this.direction = direction; }
    public void setMove(boolean move) { this.move = move; }
}