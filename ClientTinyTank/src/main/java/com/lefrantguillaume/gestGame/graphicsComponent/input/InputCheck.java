package com.lefrantguillaume.gestGame.graphicsComponent.input;

import com.lefrantguillaume.gestGame.Utils.configs.CurrentUser;
import com.lefrantguillaume.gestGame.Utils.stockage.Tuple;
import com.lefrantguillaume.gestGame.collisionComponent.CollisionController;
import com.lefrantguillaume.gestGame.gameComponent.controllers.GameController;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.obstacles.EnumObstacles;
import com.lefrantguillaume.gestGame.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gestGame.networkComponent.messages.MessageModel;
import com.lefrantguillaume.gestGame.networkComponent.messages.msg.MessageMove;
import com.lefrantguillaume.gestGame.networkComponent.messages.msg.MessagePlayerDelete;
import com.lefrantguillaume.gestGame.networkComponent.messages.msg.MessagePutObstacle;
import com.lefrantguillaume.gestGame.networkComponent.messages.msg.MessageShoot;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputCheck extends Observable {

    public int gestInput(GameController gameController, int key, EnumInput mode, int posX, int posY) {
        MessageModel request = null;

        if (Input.KEY_ESCAPE == key) {
            request = new MessagePlayerDelete(CurrentUser.getPseudo(), CurrentUser.getId());
            CurrentUser.setInGame(false);
            this.setChanged();
            this.notifyObservers(request);
            return -1;
        } else {
            Player player = gameController.getPlayer(CurrentUser.getId());
            if (player != null && player.isAlive()) {
                request = createMessageByInput(gameController.getCollisionController(), player, key, mode, posX, posY);
            }
            if (request != null) {
                this.setChanged();
                this.notifyObservers(request);
            }
            return 0;
        }
    }

    public MessageModel createMessageByInput(CollisionController collisionController, Player player, int key, EnumInput mode, int posX, int posY) {
        MessageModel message = null;

        if (key == Input.KEY_DOWN || key == Input.KEY_UP || key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
            if (mode == EnumInput.PRESSED) {
                message = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), key, true);
            } else {
                message = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), key, false);
            }
        } else if (key == Input.MOUSE_LEFT_BUTTON && mode == EnumInput.RELEASED) {
            message = new MessageShoot(CurrentUser.getPseudo(), CurrentUser.getId(), player.getTank().predictAngleHit());
        } else if (key == Input.KEY_B && mode == EnumInput.RELEASED) {
            Tuple<Float, Float, Float> boxValues = player.predictCreateBox(collisionController);
            message = new MessagePutObstacle(CurrentUser.getPseudo(), CurrentUser.getId(), EnumObstacles.WALL_WOOD, boxValues.getV1(), boxValues.getV2(), boxValues.getV3());
        }
        return message;
    }
/*

    public int keyCheck(Player player, int key, EnumInput mode) {
        MessageModel request = null;

        if (Input.KEY_ESCAPE == key) {
            request = new MessagePlayerDelete(CurrentUser.getPseudo(), CurrentUser.getId());
            CurrentUser.setInGame(false);
            this.setChanged();
            this.notifyObservers(request);
            return -1;
        } else if (player.isAlive()) {
            request = MessageFactory.createObject(key, mode);
            if (request != null) {
                this.setChanged();
                this.notifyObservers(request);
            }
        }
        return 0;
    }

    public int mouseClickCheck(Player player, int x, int y, EnumInput mode) {
        MessageModel request = null;

        if (player != null && player.isAlive()) {
            float newAngle = player.getTank().getTankState().getGunAngle();
            if (player.getTank().getTankWeapon().getShotType() == EnumShots.MACHINE_GUN) {
                newAngle += RandomTools.getInt(15) - 7.5;
                //TODO mettre cet angle dans les variables de tank
            }
            request = MessageFactory.createObject(Input.MOUSE_LEFT_BUTTON, mode, x, y, newAngle);
            if (request != null) {
                this.setChanged();
                this.notifyObservers(request);
                return 0;
            }
        }
        return -1;
    }
    */
}
