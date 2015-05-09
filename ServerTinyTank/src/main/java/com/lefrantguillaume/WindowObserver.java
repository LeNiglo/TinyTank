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
        String msg = (String) arg;
        String sub = msg.substring(1);
        switch (msg.charAt(0)) {
            case 'i':
                theInterface.addToConsoleLog(sub);
                break;
            case 'e':
                theInterface.addToConsoleErr(sub);
                break;
        }
    }
}