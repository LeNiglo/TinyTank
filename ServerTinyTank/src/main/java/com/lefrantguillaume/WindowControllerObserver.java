package com.lefrantguillaume;

import java.util.Observable;

/**
 * Created by leniglo on 04/05/15.
 */

class WindowControllerObserver extends Observable {
    public void addMessage(String msg) {
        this.setChanged();
        this.notifyObservers(msg);
    }
}