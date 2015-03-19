package com.lefrantguillaume.gameComponent.playerData;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.gameComponent.actions.EnumDirection;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerState {
    private Pair<Float, Float> positions;
    private boolean move;
    private float gunAngle;
    private EnumDirection direction;
    private User user;

    public PlayerState(User user, float x, float y) {
        this.user = user;
        positions = new Pair<Float, Float>(x, y);
        this.move = false;
        this.direction = EnumDirection.DOWN;
        this.gunAngle = this.direction.getAngle();
    }

    // GETTERS
    public User getUser() {
        return this.user;
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public boolean isMove() {
        return move;
    }

    public EnumDirection getDirection() {
        return direction;
    }

    public float getGunAngle() {
        return gunAngle;
    }

    // SETTERS
    public void setY(float y) {
        this.positions.setV2(y);
    }

    public void setX(float x) {
        this.positions.setV1(x);
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public void setDirection(EnumDirection direction) {
        this.direction = direction;
    }

    public void setGunAngle(float gunAngle) {
        this.gunAngle = gunAngle;
    }
}
