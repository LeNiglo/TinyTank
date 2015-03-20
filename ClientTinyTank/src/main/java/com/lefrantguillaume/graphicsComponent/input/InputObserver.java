package com.lefrantguillaume.graphicsComponent.input;

import com.lefrantguillaume.networkComponent.NetworkMessage;
import com.lefrantguillaume.networkComponent.messages.MessageFactory;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import sun.plugin2.message.Message;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputObserver implements Observer {
    protected NetworkMessage queue;

    protected InputObserver() {
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    protected void addInQueue(MessageModel request) {
        if (request != null) {
            queue.add(request);
        }
    }
}
