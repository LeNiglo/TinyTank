package com.lefrantguillaume.gameComponent.playerData.action;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.playerData.data.PlayerState;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.tanks.Tank;

import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerActionController extends Observable {
    private PlayerState playerState;
    private List<Shot> shots;
    private Tank tank;

    public PlayerActionController(PlayerState playerState, List<Shot> shots, Tank tank) {
        this.playerState = playerState;
        this.shots = shots;
        this.tank = tank;
    }

    public boolean doAction(PlayerAction playerAction, CollisionController collisionController) {
        if (playerAction.getAction() != EnumActions.NOTHING) {
            if (playerAction.getAction() == EnumActions.MOVE) {
                this.playerState.setMove(true);
                this.playerState.setDirection(EnumDirection.getDirectionByValue((Integer) playerAction.getValue(0)));
                collisionController.getCollisionObject(this.playerState.getUser().getId()).setAngle(this.playerState.getDirection().getAngle());
            } else if (playerAction.getAction() == EnumActions.UNMOVED && (Integer) playerAction.getValue(0) == this.playerState.getDirection().getValue()) {
                this.playerState.setMove(false);
            } else if (playerAction.getAction() == EnumActions.SHOOT) {
                Shot shot = tank.generateShot(this.playerState.getUser().getIdUser(), new Tuple<Float, Float, Float>(this.playerState.getX(),
                        this.playerState.getY(), (Float) playerAction.getValue(0)));
                CollisionObject obj = new CollisionObject(true, shot.getGraphicalX(), shot.getGraphicalY(),
                        this.tank.getShotAnimator().currentSizeAnimation(), this.playerState.getUser().getIdUser(), shot.getId(), EnumType.SHOT, shot.getAngle());
                obj.addObserver(shot);
                collisionController.addCollisionObject(obj);
                this.shots.add(shot);
            }
            else if (playerAction.getAction() == EnumActions.SPELL){

            }
        }
        this.setChanged();
        this.notifyObservers(true);
        return true;
    }
}

