package com.lefrantguillaume.gameComponent.playerData.data;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.gameComponent.playerData.action.EnumDirection;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerState {
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizeAnimation;
    private boolean move;
    private float gunAngle;
    private EnumDirection direction;
    private User user;

    public PlayerState(User user, float x, float y, Pair<Float, Float> sizeAnimation) {
        this.user = user;
        this.positions = new Pair<Float, Float>(x, y);
        this.sizeAnimation = sizeAnimation;
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

    public float getGraphicalX() {
        return this.positions.getV1() - (this.sizeAnimation.getV1() / 2);
        //    double px = Math.cos(this.direction.getAngle()) * (ax - rx) - Math.sin(this.direction.getAngle()) * (ay - ry) + rx;
    }

    public float getGraphicalY() {
        return this.positions.getV2() - (this.sizeAnimation.getV2() / 2);
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

    public Pair<Float, Float> getSizeAnimation() {
        return this.sizeAnimation;
    }

    // SETTERS

    public void setX(float x) {
        this.positions.setV1(x);
    }

    public void setY(float y) {
        this.positions.setV2(y);
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

    public void setSizeAnimation(Pair<Float, Float> sizeAnimation) {
        this.sizeAnimation = sizeAnimation;
    }
}
