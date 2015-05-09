package com.lefrantguillaume;

import java.util.Observable;

/**
 * Created by leniglo on 04/05/15.
 */

class WindowControllerObserver extends Observable {
    public void addInfo(String msg) {
        this.setChanged();
        this.notifyObservers("i" + msg);
    }
    public void addError(String msg) {
        this.setChanged();
        this.notifyObservers("e" + msg);
    }
}