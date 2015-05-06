package com.lefrantguillaume.gameComponent.playerData.action;

import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.Tank;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.playerData.data.Player;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerActionController extends Observable {
    private List<Shot> shots;
    private Tank tank;

    public PlayerActionController(List<Shot> shots, Tank tank) {
        this.shots = shots;
        this.tank = tank;
    }

    public boolean doAction(PlayerAction playerAction, CollisionController collisionController, Player player) {
        if (playerAction.getAction() != EnumActions.NOTHING) {
            if (playerAction.getAction() == EnumActions.MOVE) {
                this.tank.getTankState().setMove(true);
                this.tank.getTankState().setDirection(EnumDirection.getDirectionByValue((Integer) playerAction.getValue(0)));
                List<CollisionObject> objects = collisionController.getCollisionObject(player.getUser().getId());
                for (int i = 0; i < objects.size(); ++i) {
                    objects.get(i).setAngle(this.tank.getTankState().getDirection().getAngle());
                }
            } else if (playerAction.getAction() == EnumActions.UNMOVED && (Integer) playerAction.getValue(0) == this.tank.getTankState().getDirection().getValue()) {
                this.tank.getTankState().setMove(false);
            } else if (playerAction.getAction() == EnumActions.SHOOT) {
                Shot shot = tank.generateShot(player.getUser().getIdUser(), UUID.fromString((String)playerAction.getValue(0)), (Float) playerAction.getValue(1));

                for (int i = 0; i < shot.getCollisionObject().size(); ++i){
                    Rectangle current = shot.getCollisionObject().get(i);

                    CollisionObject obj = new CollisionObject(true, shot.getPositions(), current.getSizes(), current.getShiftOrigin(),
                            shot.getUserId(), shot.getId(), EnumType.TANK, shot.getAngle());
                    obj.addObserver(shot);
                    shot.addObserver(obj);
                    collisionController.addCollisionObject(obj);
                }
                this.shots.add(shot);
                player.getTank().getTankState().setGunAngle((Float)playerAction.getValue(1));
            }
            else if (playerAction.getAction() == EnumActions.SPELL){

            }
        }
        this.setChanged();
        this.notifyObservers(true);
        return true;
    }
}

