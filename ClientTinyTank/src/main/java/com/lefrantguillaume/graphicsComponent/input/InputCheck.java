package com.lefrantguillaume.graphicsComponent.input;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.gameComponent.playerData.PlayerState;
import com.lefrantguillaume.networkComponent.messages.MessageFactory;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputCheck extends Observable {
    public int keyCheck(int key, EnumInput mode){
        if (Input.KEY_ESCAPE == key) {
            return -1;
        }
        else if (key == Input.KEY_DOWN || key == Input.KEY_UP || key == Input.KEY_LEFT || key == Input.KEY_RIGHT)
        {
            this.setChanged();
            this.notifyObservers(key * mode.getValue());
        }
        return 0;
    }

    public int mouseClickCheck(PlayerState player, int x, int y, EnumInput mode){
        double angle = MathTools.getAngle(player.getX(), player.getY(), x, y);

        //MessageModel request = MessageFactory.createObject(value, mode);
        Debug.debug("x:"+x+" y:"+y);
        this.setChanged();
        this.notifyObservers(Input.KEY_A * mode.getValue());
        return 0;
    }
}
