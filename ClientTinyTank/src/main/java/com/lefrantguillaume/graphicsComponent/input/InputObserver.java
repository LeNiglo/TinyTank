package com.lefrantguillaume.graphicsComponent.input;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputObserver extends Observable implements Observer {
    protected List<String> queue;
    protected InputObserver(){}

    @Override
    public void update(Observable o, Object arg) {
    }

    protected void addInQueue(String value){
        queue.add(value);
        setChanged();
        notifyObservers(true);
    }
}
