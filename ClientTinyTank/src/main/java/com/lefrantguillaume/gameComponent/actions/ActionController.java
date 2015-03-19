package com.lefrantguillaume.gameComponent.actions;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.playerData.PlayerState;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.tanks.Tank;

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
        this.idsShot = 10;
    }

    public boolean doAction(Action action, CollisionController collisionController) {
        Debug.debug(String.valueOf(action.getAction()));
        if (action.getAction() != EnumActions.NOTHING) {
            if (action.getAction() == EnumActions.MOVE) {
                this.playerState.setMove(true);
                this.playerState.setDirection(EnumDirection.getDirectionByValue((Integer) action.getValue(0)));
            } else if (action.getAction() == EnumActions.UNMOVED && (Integer) action.getValue(0) == this.playerState.getDirection().getValue()) {
                this.playerState.setMove(false);
            } else if (action.getAction() == EnumActions.SHOOT) {
                Shot shot = tank.generateShot(this.playerState.getUser().getId(), this.idsShot, new Tuple<Float, Float, Float>(this.playerState.getX(), this.playerState.getY(), this.playerState.getDirection().getAngle()));
                CollisionObject obj = new CollisionObject(true, this.playerState.getX(), this.playerState.getY(),
                        this.tank.getTankAnimator().currentAnimation().getCurrentFrame().getWidth(),
                        this.tank.getTankAnimator().currentAnimation().getCurrentFrame().getHeight(), this.playerState.getUser().getId(), this.getIdsShot(), EnumType.SHOT);
                obj.addObserver(shot);
                collisionController.addCollisionObject(obj);
                this.shots.add(shot);
                this.idsShot += 1;
            }
            else if (action.getAction() == EnumActions.SPELL){

            }
        }
        this.setChanged();
        this.notifyObservers(true);
        return true;
    }

    public int getIdsShot() {
        return this.idsShot;
    }
}

