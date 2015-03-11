package com.lefrantguillaume.graphicsComponent.input;

import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputHomeObserver extends InputObserver {
    public InputHomeObserver(List<String> queue) {
        this.queue = queue;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("input catch: " + arg);
        this.addInQueue(String.valueOf(arg));
    }
}
