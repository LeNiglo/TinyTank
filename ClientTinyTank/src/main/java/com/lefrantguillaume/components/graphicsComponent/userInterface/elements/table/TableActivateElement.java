package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.table;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundScore;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

/**
 * Created by andres_k on 02/07/2015.
 */
public class TableActivateElement extends TableElement {
    private int toActivate;

    public TableActivateElement(EnumOverlayElement type, BodyRect body, int toActivate) {
        super(type, body, false, new boolean[]{true, true});
        this.toActivate = toActivate;
    }

    // FUNCTION
    @Override
    public void doTask(Object task) {
        if (task instanceof Element) {
            this.addElement((Element) task);
        } else if (task instanceof Pair) {
            Pair<Integer, Boolean> received = (Pair<Integer, Boolean>) task;
            if (received.getV1() < this.reachable.length) {
                this.reachable[received.getV1()] = received.getV2();
            }
        } else if (this.type == EnumOverlayElement.TABLE_STAT) {
            if (task instanceof MessageRoundScore) {
                MessageRoundScore message = (MessageRoundScore) task;

                Element element = new StringElement(new StringTimer(message.getPseudo() + " : " + String.valueOf(message.getScore() + " pts.")), Color.black, message.getTeamId() + ":" + message.getId(), Element.PositionInBody.MIDDLE_MID);
                this.addElement(element);
            }
        }
    }

    @Override
    public Object eventPressed(int key, char c) {
        if (key == this.toActivate) {
            return true;
        }
        return null;
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (key == this.toActivate) {
            if (this.isActivated()) {
                this.stop();
            } else {
                this.start();
            }
            return true;
        } else if (key == Input.KEY_ESCAPE){
            this.stop();
        }
        return null;
    }

}
