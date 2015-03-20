package com.lefrantguillaume.gameComponent.playerData.action;

import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.MessageMove;
import com.lefrantguillaume.networkComponent.messages.msg.MessageShoot;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class PlayerAction {
    private EnumActions action;
    List<Object> values;

    public PlayerAction(MessageModel todo) {
        this.action = EnumActions.NOTHING;
        this.values = new ArrayList<Object>();
        if (todo instanceof MessageMove) {
            if (((MessageMove) todo).isMove() == true) {
                this.action = EnumActions.MOVE;
            } else {
                this.action = EnumActions.UNMOVED;
            }
            this.values.add(new Integer(((MessageMove) todo).getDirection()));
        }
        else if (todo instanceof MessageShoot){
            if (((MessageShoot) todo).getValueKeyPressed() == Input.MOUSE_LEFT_BUTTON) {
                this.action = EnumActions.SHOOT;
                this.values.add(((MessageShoot)todo).getAngle());
            }
        }
    }

    public Object getValue(int index) {
        return this.values.get(index);
    }

    public EnumActions getAction() {
        return this.action;
    }
}
