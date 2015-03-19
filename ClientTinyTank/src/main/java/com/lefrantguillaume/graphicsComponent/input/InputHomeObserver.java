package com.lefrantguillaume.graphicsComponent.input;

import com.lefrantguillaume.networkComponent.NetworkMessage;

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
        System.out.println("input catch: " + arg);
        int value = (Integer) arg;
        if (value > 0) {
            this.addInQueue(value, EnumInput.PRESSED);
        } else {
            this.addInQueue(value * EnumInput.RELEASED.getValue(), EnumInput.RELEASED);
        }
    }
}
