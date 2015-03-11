package com.lefrantguillaume.networkComponent;

import com.lefrantguillaume.Utils.NetworkConfig;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkController implements Observer {

    private List<String> queue;
    private NetworkCall networkCall;
    private int index;

    public NetworkController(List<String> queue, NetworkConfig config) {
        this.queue = queue;
        this.networkCall = new NetworkCall(config);
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
