package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.table;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundEnd;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundStart;

/**
 * Created by andres_k on 02/07/2015.
 */
public class TableAppearElement extends TableElement {

    public TableAppearElement(EnumOverlayElement type, BodyRect body) {
        super(type, body, false, new boolean[]{true, true});
    }

    // FUNCTION
    @Override
    public void doTask(Object task) {
        Debug.debug("\nReceived in " + this.type + " -> " + task);
        if (task instanceof Element) {
            this.addElement((Element) task);
        } else if (task instanceof Pair) {
            Pair<Integer, Boolean> received = (Pair<Integer, Boolean>) task;
            if (received.getV1() < this.reachable.length) {
                this.reachable[received.getV1()] = received.getV2();
            }
        } else if (this.type == EnumOverlayElement.TABLE_ROUND_NEW) {
            if (task instanceof MessageRoundStart) {
                MessageRoundStart message = (MessageRoundStart) task;

                if (message.isStarted() == true) {
                    this.sendTaskToAll("start");
                    this.activatedTimer.startTimer();
                } else {
                    this.activatedTimer.stopTimer();
                }
            }
        } else if (this.type == EnumOverlayElement.TABLE_ROUND_END) {
            if (task instanceof MessageRoundEnd) {
                MessageRoundEnd message = (MessageRoundEnd) task;
                this.sendTaskToAll("start");
                if (CurrentUser.isInGame()) {
                    if (CurrentUser.getIdTeam().equals(message.getWinnerTeam())) {
                        this.sendTaskToAll(new Pair<>("newCurrentIndex", 0));
                    } else {
                        this.sendTaskToAll(new Pair<>("newCurrentIndex", 1));
                    }
                }
                this.activatedTimer.startTimer();
            } else if (task instanceof MessageRoundStart) {
                MessageRoundStart message = (MessageRoundStart) task;
                if (message.isStarted() == true) {
                    this.activatedTimer.stopTimer();
                }
            }
        }
    }
}
