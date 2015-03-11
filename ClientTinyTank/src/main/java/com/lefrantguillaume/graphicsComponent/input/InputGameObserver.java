package com.lefrantguillaume.graphicsComponent.input;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputGameObserver extends InputObserver {
    public InputGameObserver(List<String> queue, Observer observer) {
        this.queue = queue;
        this.addObserver(observer);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("input catch: " + arg);
        this.addInQueue(String.valueOf(arg));
    }
}
