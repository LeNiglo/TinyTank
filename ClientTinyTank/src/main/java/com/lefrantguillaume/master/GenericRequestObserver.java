package com.lefrantguillaume.master;

import com.lefrantguillaume.networkComponent.networkGame.NetworkMessage;
import com.lefrantguillaume.networkComponent.networkGame.messages.MessageModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 11/03/2015.
 */
public class GenericRequestObserver implements Observer {
    private NetworkMessage queue;

    public GenericRequestObserver(NetworkMessage queue) {
        this.queue = queue;
    }

    public void update(Observable o, Object arg) {
        MessageModel request = (MessageModel) arg;
        if (request != null)
            this.queue.add(request);
    }
}
