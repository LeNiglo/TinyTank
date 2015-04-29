package com.lefrantguillaume.gestGame.master;

import com.lefrantguillaume.gestGame.networkComponent.NetworkMessage;
import com.lefrantguillaume.gestGame.networkComponent.messages.MessageModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MasterResponseController extends Observable implements Observer {
    private NetworkMessage queue;
    private int index;

    public MasterResponseController(NetworkMessage queue) {
        this.queue = queue;
        this.index = queue.size() - 1;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.queue.add((MessageModel)arg);
        if (this.queue.isEmpty() == false) {
            this.index = this.queue.size() - 1;
            this.setChanged();
            this.notifyObservers(this.queue.get(index));

            this.queue.remove(index);
        }
    }
}
