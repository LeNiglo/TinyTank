package com.lefrantguillaume.graphicsComponent.input;

import com.lefrantguillaume.networkComponent.NetworkMessage;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputHomeObserver extends InputObserver {
    public InputHomeObserver(NetworkMessage queue) {
        this.queue = queue;
    }

    @Override
    public void update(Observable o, Object arg) {
        MessageModel request = (MessageModel) arg;
        this.addInQueue(request);
    }
}
