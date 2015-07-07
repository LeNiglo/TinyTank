package com.lefrantguillaume.components.gameComponent.playerData.action;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.graphicsComponent.sounds.EnumSound;
import com.lefrantguillaume.components.graphicsComponent.sounds.SoundController;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerActionController { //extends Observable {
    private List<Shot> shots;

    public PlayerActionController(List<Shot> shots) {
        this.shots = shots;
    }

    public List<Object> doAction(PlayerAction playerAction, CollisionController collisionController, Player player) {
        List<Object> result = new ArrayList<>();
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
            } else if (playerAction.getAction() == EnumActions.UNMOVED) {
                if ((Integer) playerAction.getValue(0) == player.getTank().getTankState().getDirection().getValue()) {
                    player.getTank().getTankState().setMove(false);
                    player.getTank().getTankState().setX((float) (playerAction.getValue(1)));
                    player.getTank().getTankState().setY((float) (playerAction.getValue(2)));
                    List<CollisionObject> objects = collisionController.getCollisionObject(player.getUser().getId());
                    for (CollisionObject object : objects) {
                        object.setX(player.getTank().getTankState().getX());
                        object.setY(player.getTank().getTankState().getY());
                    }
                } else if ((float) playerAction.getValue(1) == -100){
                    player.getTank().getTankState().setMove(false);
                }
            } else if (playerAction.getAction() == EnumActions.SHOOT) {
                if (this.getShot((String) playerAction.getValue(0)) == null) {
                    if (player.getTank().getTankWeapon().isActivated() && CurrentUser.getId().equals(player.getUser().getIdUser())) {
                        result.add(new Pair<>(EnumOverlayElement.TABLE_ICON, new Pair<>(EnumOverlayElement.HIT, player.getTank().getTankWeapon().getCooldown())));
                    }
                    SoundController.play(EnumSound.getSound(player.getTank().getTankWeapon().getShotType()));
                    if (player.getTank().getTankWeapon().getShotType() == EnumGameObject.LASER) {
                        player.setCanDoAction(false);
                        this.addShootTimer(playerAction, collisionController, player);
                    } else {
                        this.generateShot(playerAction, collisionController, player);
                    }
                }
            } else if (playerAction.getAction() == EnumActions.SPELL) {
                if (player.getTank().getTankSpell().isActivated() && CurrentUser.getId().equals(player.getUser().getIdUser())) {
                    result.add(new Pair<>(EnumOverlayElement.TABLE_ICON, new Pair<>(EnumOverlayElement.SPELL, player.getTank().getTankSpell().getCooldown())));
                }
                Object item = player.getTank().activeSpell();
                if (item instanceof Obstacle) {
                    Obstacle obstacle = (Obstacle) item;
                    TankState state = player.getTank().getTankState();
                    obstacle.createObstacle(player.getUser().getId(), player.getUser().getPseudo(), (String) playerAction.getValue(0), 0, state.getX(), state.getY());
                    result.add(item);

                    if (obstacle.getType() == EnumGameObject.SHIELD && CurrentUser.getId().equals(obstacle.getPlayerId())) {
                        Pair order = new Pair<>(EnumOverlayElement.USER_SHIELD, new Pair<>("cutBody", obstacle.getPercentageLife()));
                        result.add(new Pair<>(EnumOverlayElement.GENERIC_USER_STAT, order));
                    }
                }
            }
        }
        return result;
    }

    private void generateShot(PlayerAction playerAction, CollisionController collisionController, Player player) {
        Shot shot = player.getTank().generateShot(player.getUser().getIdUser(), (String) playerAction.getValue(0), (Float) playerAction.getValue(1));

        if (shot != null) {
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
    }

    private void addShootTimer(PlayerAction playerAction, CollisionController collisionController, Player player) {
        player.getTank().getTankSpell().stopCurrentSpell();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                generateShot(playerAction, collisionController, player);
            }
        }, 500);
    }

    private Shot getShot(String id) {
        for (Shot shot : this.shots) {
            if (shot.getId().equals(id)) {
                return shot;
            }
        }
        return null;
    }
}

