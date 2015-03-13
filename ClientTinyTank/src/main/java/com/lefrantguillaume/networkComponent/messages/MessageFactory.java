package com.lefrantguillaume.networkComponent.messages;

import com.lefrantguillaume.Utils.configs.User;
import com.lefrantguillaume.networkComponent.messages.msg.MessageMove;
import com.lefrantguillaume.networkComponent.messages.msg.MessageShoot;
import org.newdawn.slick.Input;

/**
 * Created by andres_k on 13/03/2015.
 */
public class MessageFactory {
    public static MessageModel createObject(int value, int mode) {
        MessageModel object;
        if (value == Input.KEY_DOWN || value == Input.KEY_UP || value == Input.KEY_LEFT || value == Input.KEY_RIGHT) {
            if (mode == 1) {
                object = new MessageMove(User.getPseudo(), User.getId(), value, true);
            } else {
                object = new MessageMove(User.getPseudo(), User.getId(), value, false);
            }
        }
        else if (value == Input.KEY_A && mode == -1) {
            object = new MessageShoot(User.getPseudo(), User.getId(), value);
        }
        else
            return null;
        return object;
    }
}