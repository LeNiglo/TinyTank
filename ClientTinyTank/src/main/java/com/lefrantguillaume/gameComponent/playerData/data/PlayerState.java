package com.lefrantguillaume.gameComponent.playerData.data;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.gameComponent.playerData.action.EnumDirection;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerState {
    private boolean move;
    private float gunAngle;
    private EnumDirection direction;
    private User user;

    public PlayerState(User user) {
        this.user = user;
        this.move = false;
        this.direction = EnumDirection.DOWN;
        this.gunAngle = this.direction.getAngle();
    }

    // GETTERS
    public User getUser() {
        return this.user;
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
