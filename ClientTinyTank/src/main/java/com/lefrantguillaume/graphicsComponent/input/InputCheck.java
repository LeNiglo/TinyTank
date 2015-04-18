package com.lefrantguillaume.graphicsComponent.input;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.playerData.data.PlayerState;
import com.lefrantguillaume.networkComponent.messages.MessageFactory;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerDelete;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputCheck extends Observable {
    public int keyCheck(int key, EnumInput mode){
        if (Input.KEY_ESCAPE == key) {
            MessageModel request = new MessagePlayerDelete(CurrentUser.getPseudo(), CurrentUser.getId());
            CurrentUser.setInGame(false);
            this.setChanged();
            this.notifyObservers(request);
            return -1;
        }
        else if (key == Input.KEY_DOWN || key == Input.KEY_UP || key == Input.KEY_LEFT || key == Input.KEY_RIGHT)
        {
            MessageModel request = MessageFactory.createObject(key, mode);
            this.setChanged();
            this.notifyObservers(request);
        }
        return 0;
    }

    public int mouseClickCheck(Player player, int x, int y, EnumInput mode){
        if (player != null) {

            MessageModel request = MessageFactory.createObject(Input.MOUSE_LEFT_BUTTON, mode, x, y, player.getTank().getTankState().getGunAngle());
            this.setChanged();
            this.notifyObservers(request);
            return 0;
        }
        return -1;
    }
}
