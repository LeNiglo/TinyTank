package com.lefrantguillaume.components.gameComponent.playerData.action;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageMove;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageShoot;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageSpell;

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
        this.values = new ArrayList<>();
        this.createAction(todo);
    }

    public void createAction(MessageModel todo) {
        if (todo instanceof MessageMove) {
            if (((MessageMove) todo).isMove() == true) {
                this.action = EnumActions.MOVE;
            } else {
                this.action = EnumActions.UNMOVED;
            }
            this.values.add(new Integer(((MessageMove) todo).getDirection()));
            this.values.add(new Float(((MessageMove) todo).getPlayerX()));
            this.values.add(new Float(((MessageMove) todo).getPlayerY()));
        } else if (todo instanceof MessageShoot) {
            this.action = EnumActions.SHOOT;
            this.values.add(((MessageShoot) todo).getShotId());
            this.values.add(((MessageShoot) todo).getAngle());
        } else if (todo instanceof MessageSpell) {
            this.action = EnumActions.SPELL;
            this.values.add(((MessageSpell) todo).getSpellId());
        }
    }

    public Object getValue(int index) {
        return this.values.get(index);
    }

    public EnumActions getAction() {
        return this.action;
    }
}
