package com.lefrantguillaume;

import com.lefrantguillaume.ui.IInterface;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by leniglo on 04/05/15.
 */


public class WindowObserver implements Observer {
    private final IInterface theInterface;

    public WindowObserver(IInterface window) {
        this.theInterface = window;
    }

    @Override
    public void update(Observable o, Object arg) {
        theInterface.addToConsoleLog((String) arg);
    }
}