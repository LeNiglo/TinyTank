package com.lefrantguillaume.master;

import com.lefrantguillaume.networkComponent.NetworkCall;
import com.lefrantguillaume.networkComponent.NetworkMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 11/03/2015.
 */
public class MasterRequestController implements Observer {

    private NetworkMessage queue;
    private NetworkCall networkCall;
    private int index;

    public MasterRequestController(NetworkMessage queue, NetworkCall networkCall) {
        this.queue = queue;
        this.networkCall = networkCall;
        this.index = queue.size() - 1;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.index = this.queue.size() - 1;
        if (this.queue.isEmpty() == false) {
            this.networkCall.call(queue.get(index));
            this.queue.remove(index);
        }
    }
}
