package com.lefrantguillaume.components.graphicsComponent.input;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.gameComponent.controllers.GameController;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageMove;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessagePlayerDelete;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessagePutObstacle;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageShoot;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputGame extends Observable{
    private InputData inputData;

    public InputGame(String configsFile) throws JSONException {
        this.inputData = new InputData(configsFile);
    }

    public int checkInput(GameController gameController, int key, EnumInput mode, int posX, int posY) {
        MessageModel request = null;
        String keyName;

        if (key == -2) {
            keyName = "MOUSE_LEFT_BUTTON";
        } else if (key == -3) {
            keyName = "MOUSE_RIGHT_BUTTON";
        } else {
            keyName = Input.getKeyName(key);
        }
        if (keyName.equals(this.inputData.getInputValue(EnumInput.ESCAPE)) && mode == EnumInput.RELEASED) {
            request = new MessagePlayerDelete(CurrentUser.getPseudo(), CurrentUser.getId());
            CurrentUser.setInGame(false);
            this.setChanged();
            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INPUT, EnumTargetTask.MESSAGE_SERVER, request));
            return -1;
        } else {
            Player player = gameController.getPlayer(CurrentUser.getId());
            if (player != null && player.isAlive()) {
                request = createMessageByInput(gameController.getCollisionController(), player, keyName, mode, posX, posY);
            }
            if (request != null) {
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INPUT, EnumTargetTask.MESSAGE_SERVER, request));
            }
            return 0;
        }
    }

    public MessageModel createMessageByInput(CollisionController collisionController, Player player, String keyName, EnumInput mode, int posX, int posY) {
        MessageModel message = null;

        if (keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_UP)) || keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_DOWN))
                || keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_RIGHT)) || keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_LEFT))) {
            if (mode == EnumInput.PRESSED) {
                message = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName)), true);
            } else {
                message = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName)), false);
            }
        } else if (keyName.equals(this.inputData.getInputValue(EnumInput.SHOOT)) && mode == EnumInput.RELEASED) {
            message = new MessageShoot(CurrentUser.getPseudo(), CurrentUser.getId(), player.getTank().predictAngleHit());
        } else if (keyName.equals(this.inputData.getInputValue(EnumInput.PUT_OBJECT)) && mode == EnumInput.RELEASED) {
            Tuple<Float, Float, Float> boxValues = player.predictCreateBox(collisionController);
            message = new MessagePutObstacle(CurrentUser.getPseudo(), CurrentUser.getId(), EnumGameObject.IRON_WALL, boxValues.getV1(), boxValues.getV2(), boxValues.getV3());
        }
        return message;
    }
}