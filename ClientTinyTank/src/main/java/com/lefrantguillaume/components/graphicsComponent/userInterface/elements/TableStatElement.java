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
public class TableStatElement extends TableElement {

    public TableStatElement(EnumOverlayElement type, BodyRect body) {
        super(type, body, true, true);
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
}
