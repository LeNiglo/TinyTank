package com.lefrantguillaume.gameComponent.actions;

import com.lefrantguillaume.Utils.Debug;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.EnumDirection;
import com.lefrantguillaume.gameComponent.PlayerState;
import com.lefrantguillaume.gameComponent.tanks.EnumType;
import com.lefrantguillaume.gameComponent.tanks.Tank;

import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 13/03/2015.
 */
public class ActionController extends Observable {
    private PlayerState playerState;
    private List<Shot> shots;
    private Tank tank;
    private int idsShot;

    public ActionController(PlayerState playerState, List<Shot> shots, Tank tank) {
        this.playerState = playerState;
        this.shots = shots;
        this.tank = tank;
        this.idsShot = 20;
    }

    public boolean doAction(Action action, CollisionController collisionController) {
        int indexAnimation = 0;
        Debug.debug(String.valueOf(action.getAction()));
        if (action.getAction() != EnumActions.NOTHING) {
            if (action.getAction() == EnumActions.MOVE) {
                this.playerState.setMove(true);
                this.playerState.setDirection(EnumDirection.getDirectionByValue((Integer) action.getValue(0)));
            } else if (action.getAction() == EnumActions.UNMOVED && (Integer) action.getValue(0) == this.playerState.getDirection().getValue()) {
                this.playerState.setMove(false);
            } else if (action.getAction() == EnumActions.SHOOT) {
                Shot shot = tank.generateShot(this.playerState.getUserId(), this.idsShot, new Tuple<Float, Float, Float>(this.playerState.getX(), this.playerState.getY(), this.playerState.getDirection().getAngle()));
                CollisionObject obj = new CollisionObject(this.playerState.getX(), this.playerState.getY(),
                        this.tank.getTankAnimator().currentAnimation().getCurrentFrame().getWidth(),
                        this.tank.getTankAnimator().currentAnimation().getCurrentFrame().getHeight(), this.playerState.getUserId(), this.getIdsShot(), EnumType.SHOT);
                obj.addObserver(shot);
                collisionController.addCollisionObject(obj);
                this.shots.add(shot);
                this.idsShot += 1;
            }
        }
        this.setChanged();
        this.notifyObservers(indexAnimation);
        return true;
    }

    public int getIdsShot() {
        return this.idsShot;
    }
}

