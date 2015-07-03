package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundState;

/**
 * Created by andres_k on 02/07/2015.
 */
public class TableAppearElement extends TableElement {

    public TableAppearElement(EnumOverlayElement type, BodyRect body) {
        super(type, body, false, new boolean[]{true, true});
    }

    // FUNCTION
    @Override
    public void doTask(Object task){
        if (task instanceof Element) {
            this.addElement((Element)task);
        } else if (this.type == EnumOverlayElement.TABLE_NEW_ROUND) {
            if (task instanceof MessageRoundState) {
                MessageRoundState message = (MessageRoundState) task;
                if (message.isStarted() == false) {
                    this.sendTaskToAll("start");
                    this.activatedTimer.startTimer();
                } else {
                    this.activatedTimer.stopTimer();
                }
            }
        }
    }
}
