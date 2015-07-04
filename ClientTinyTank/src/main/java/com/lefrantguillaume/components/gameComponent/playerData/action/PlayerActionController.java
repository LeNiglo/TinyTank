package com.lefrantguillaume.components.gameComponent.playerData.action;

import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;

import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerActionController extends Observable {
    private List<Shot> shots;

    public PlayerActionController(List<Shot> shots) {
        this.shots = shots;
    }

    public Object doAction(PlayerAction playerAction, CollisionController collisionController, Player player) {
        if (playerAction.getAction() != EnumActions.NOTHING) {
            if (playerAction.getAction() == EnumActions.MOVE) {
                player.getTank().getTankState().setMove(true);
                player.getTank().getTankState().setDirection(EnumDirection.getDirectionByValue((Integer) playerAction.getValue(0)));
                player.getTank().getTankState().setX((float) (playerAction.getValue(1)));
                player.getTank().getTankState().setY((float) (playerAction.getValue(2)));

                List<CollisionObject> objects = collisionController.getCollisionObject(player.getUser().getId());
                for (CollisionObject object : objects) {
                    object.setAngle(player.getTank().getTankState().getDirection().getAngle());
                    object.setX(player.getTank().getTankState().getX());
                    object.setY(player.getTank().getTankState().getY());
                }
            } else if (playerAction.getAction() == EnumActions.UNMOVED && (Integer) playerAction.getValue(0) == player.getTank().getTankState().getDirection().getValue()) {
                player.getTank().getTankState().setMove(false);
                player.getTank().getTankState().setX((float) (playerAction.getValue(1)));
                player.getTank().getTankState().setY((float) (playerAction.getValue(2)));

                List<CollisionObject> objects = collisionController.getCollisionObject(player.getUser().getId());
                for (CollisionObject object : objects) {
                    object.setX(player.getTank().getTankState().getX());
                    object.setY(player.getTank().getTankState().getY());
                }
            } else if (playerAction.getAction() == EnumActions.SHOOT) {
                if (this.getShot((String) playerAction.getValue(0)) == null)
                if (player.getTank().getTankWeapon().getShotType() == EnumGameObject.LASER) {
                    player.setCanDoAction(false);
                    this.addShootTimer(playerAction, collisionController, player);
                } else {
                    this.generateShot(playerAction, collisionController, player);
                }
            } else if (playerAction.getAction() == EnumActions.SPELL) {
                if (player.getTank().isSpellActivated() == false) {
                    Object result = player.getTank().activeSpell();
                    if (result instanceof Obstacle) {
                        TankState state = player.getTank().getTankState();
                        ((Obstacle) result).createObstacle(player.getUser().getId(), player.getUser().getPseudo(), (String) playerAction.getValue(0), 0, state.getX(), state.getY());
                    }
                    return result;
                }
            }

        }
        this.setChanged();
        this.notifyObservers(true);
        return true;
    }

    private void generateShot(PlayerAction playerAction, CollisionController collisionController, Player player) {
        Shot shot = player.getTank().generateShot(player.getUser().getIdUser(), (String) playerAction.getValue(0), (Float) playerAction.getValue(1));

        for (int i = 0; i < shot.getCollisionObject().size(); ++i) {
            Block current = shot.getCollisionObject().get(i);

            CollisionObject obj = new CollisionObject(shot.getIgnoredObjectList(), shot.getPositions(), current.getSizes(), current.getShiftOrigin(),
                    shot.getUserId(), shot.getId(), shot.getType(), shot.getAngle());
            obj.addObserver(shot);
            shot.addObserver(obj);
            collisionController.addCollisionObject(obj);
        }
        this.shots.add(shot);
        player.getTank().getTankState().setGunAngle((Float) playerAction.getValue(1));
    }

    private void addShootTimer(PlayerAction playerAction, CollisionController collisionController, Player player) {
        player.getTank().getTankSpell().stopCurrentSpell();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                generateShot(playerAction, collisionController, player);
            }
        }, 2000);
    }

    private Shot getShot(String id){
        for (Shot shot : this.shots){
            if (shot.getId().equals(id)){
                return shot;
            }
        }
        return null;
    }
}

