package com.lefrantguillaume;

import com.lefrantguillaume.ui.ServerGUI;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by leniglo on 04/05/15.
 */


class WindowObserver implements Observer {
    private final ServerGUI window;

    public WindowObserver(ServerGUI window) {
        this.window = window;
    }

    @Override
    public void update(Observable o, Object arg) {
        window.addToConsoleLog((String) arg);
    }
}