package com.lefrantguillaume.gestGame.networkComponent.messages;

import com.lefrantguillaume.gestGame.Utils.configs.CurrentUser;
import com.lefrantguillaume.gestGame.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.gestGame.networkComponent.messages.msg.MessageMove;
import com.lefrantguillaume.gestGame.networkComponent.messages.msg.MessageShoot;
import org.newdawn.slick.Input;

/**
 * Created by andres_k on 13/03/2015.
 */
public class MessageFactory {
    public static MessageModel createObject(int value, EnumInput mode) {
        MessageModel object = null;
        if (value == Input.KEY_DOWN || value == Input.KEY_UP || value == Input.KEY_LEFT || value == Input.KEY_RIGHT) {
            if (mode == EnumInput.PRESSED) {
                object = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), value, true);
            } else {
                object = new MessageMove(CurrentUser.getPseudo(), CurrentUser.getId(), value, false);
            }
        }
        return object;
    }

    public static MessageModel createObject(int value, EnumInput mode, float x, float y, float angleDirection) {
        MessageModel object = null;
         if (value == Input.MOUSE_LEFT_BUTTON&& mode == EnumInput.RELEASED) {
            object = new MessageShoot(CurrentUser.getPseudo(), CurrentUser.getId(), angleDirection);
        }
        return object;
    }
}