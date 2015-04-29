package com.lefrantguillaume.graphicsComponent.input;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.RandomTools;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.EnumShots;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.networkComponent.messages.MessageFactory;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerDelete;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputCheck extends Observable {
    public int keyCheck(Player player, int key, EnumInput mode) {
        if (Input.KEY_ESCAPE == key) {
            MessageModel request = new MessagePlayerDelete(CurrentUser.getPseudo(), CurrentUser.getId());
            CurrentUser.setInGame(false);
            this.setChanged();
            this.notifyObservers(request);
            return -1;
        }
        if (player.isAlive()) {
            if (key == Input.KEY_DOWN || key == Input.KEY_UP || key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
                MessageModel request = MessageFactory.createObject(key, mode);
                this.setChanged();
                this.notifyObservers(request);
            }
        }
        return 0;
    }

    public int mouseClickCheck(Player player, int x, int y, EnumInput mode) {
        if (player != null && player.isAlive()) {
            float newAngle = player.getTank().getTankState().getGunAngle();
            if (player.getTank().getTankWeapon().getShotType() == EnumShots.MACHINE_GUN){
                newAngle += RandomTools.getInt(15) - 7.5;
                //TODO mettre cet angle dans les variables de tank
            }
            MessageModel request = MessageFactory.createObject(Input.MOUSE_LEFT_BUTTON, mode, x, y, newAngle);
            this.setChanged();
            this.notifyObservers(request);
            return 0;
        }
        return -1;
    }
}
