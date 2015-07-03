package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundScore;
import org.newdawn.slick.Color;

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
    public void doTask(Object task){
        if (task instanceof Element) {
            this.addElement((Element)task);
        } else if (this.type == EnumOverlayElement.TABLE_STAT) {
            if (task instanceof MessageRoundScore) {
                MessageRoundScore message = (MessageRoundScore)task;

                Element element = new StringElement(new StringTimer(message.getPseudo() + " : " + String.valueOf(message.getScore() + " pts.")), Color.black, message.getTeamId() + ":" + message.getId(), Element.PositionInBody.MIDDLE_MID);
                this.addElement(element);
            }
        }
    }

    @Override
    public Object eventPressed(int key, char c) {
        if (key == this.toActivate){
            return true;
        }
        return null;
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (key == this.toActivate){
            if (this.isActivated()){
                this.stop();
            } else {
                this.start();
            }
            return true;
        }
        return null;
    }

}
