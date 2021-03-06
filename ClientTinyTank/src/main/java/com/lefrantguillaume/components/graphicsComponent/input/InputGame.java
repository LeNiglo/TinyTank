package com.lefrantguillaume.components.graphicsComponent.input;

import com.lefrantguillaume.utils.configs.CurrentUser;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.components.gameComponent.controllers.GameController;
import com.lefrantguillaume.components.gameComponent.playerData.action.EnumDirection;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageMove;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessagePutObstacle;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageShoot;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageSpell;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputGame extends Observable {
    private InputData inputData;
    private boolean valid;

    public InputGame(InputData inputData) throws JSONException {

        if (inputData != null) {
            this.inputData = inputData;
            this.valid = true;
        } else {
            this.valid = false;
        }
    }

    public int checkInput(GameController gameController, int key, EnumInput mode, int posX, int posY) {
        MessageModel request = null;
        String keyName;

        if (this.valid == false)
            return -1;
        if (key == -2) {
            keyName = "MOUSE_LEFT_BUTTON";
        } else if (key == -3) {
            keyName = "MOUSE_RIGHT_BUTTON";
        } else {
            keyName = Input.getKeyName(key);
        }
        if ((keyName.equals(this.inputData.getInputValue(EnumInput.OVERLAY_1)) || keyName.equals(this.inputData.getInputValue(EnumInput.OVERLAY_2)))&& mode == EnumInput.RELEASED) {
            return EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName));
        } else if (gameController != null) {
            Player player = gameController.getPlayer(CurrentUser.getId());
            if (player != null && player.isAlive() && player.isCanDoAction()) {
                request = createMessageByInput(gameController, player, keyName, mode, posX, posY);
            }
            if (request != null) {
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INPUT, EnumTargetTask.MESSAGE_SERVER, request));
            }
        }
        return 0;
    }

    public MessageModel createMessageByInput(GameController gameController, Player player, String keyName, EnumInput mode, int posX, int posY) {
        MessageModel message = null;

        if (keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_UP)) || keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_DOWN))
                || keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_RIGHT)) || keyName.equals(this.inputData.getInputValue(EnumInput.MOVE_LEFT))) {
            if (mode == EnumInput.PRESSED) {
                if (!player.getTank().getTankState().isMove() ||
                        (player.getTank().getTankState().getDirection() != EnumDirection.getDirectionByValue(EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName))))) {
                    message = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName)), true,
                            player.getTank().getTankState().getX(), player.getTank().getTankState().getY());
                }
            } else {
                message = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName)), false,
                        player.getTank().getTankState().getX(), player.getTank().getTankState().getY());
            }
        } else if (keyName.equals(this.inputData.getInputValue(EnumInput.SHOOT)) && mode == EnumInput.RELEASED) {
            if (player.getTank().getTankWeapon().isActivated()) {
                message = new MessageShoot(CurrentUser.getPseudo(), CurrentUser.getId(), player.getTank().predictAngleHit());
            }
        } else if (keyName.equals(this.inputData.getInputValue(EnumInput.PUT_OBJECT)) && mode == EnumInput.RELEASED) {
            Tuple<Float, Float, Float> boxValues = player.predictCreateBox(gameController.getCollisionController(), gameController.getObstacleConfigData());
            if (boxValues != null && player.getTank().getTankBox().isActivated()) {
                message = new MessagePutObstacle(CurrentUser.getPseudo(), CurrentUser.getId(), player.getTank().getTankBox().getType(), boxValues.getV1(), boxValues.getV2(), boxValues.getV3());
            }
        } else if (keyName.equals(this.inputData.getInputValue(EnumInput.SPELL)) && mode == EnumInput.RELEASED) {
            if (player.getTank().getTankSpell().isActivated()) {
                message = new MessageSpell(CurrentUser.getPseudo(), CurrentUser.getId(), player.getTank().getTankSpell().getType(), player.getTank().getTankState().getGunAngle(), posX, posY);
            }
        }
        return message;
    }
}