package com.lefrantguillaume;

import com.lefrantguillaume.userInterface.UserInterface;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by leniglo on 04/05/15.
 */


public class WindowObserver implements Observer {
    private final UserInterface userInterface;

    public WindowObserver(UserInterface window) {
        this.userInterface = window;
    }

    @Override
    public void update(Observable o, Object arg) {
        String msg = (String) arg;
        String sub = msg.substring(1);
        switch (msg.charAt(0)) {
            case 'i':
                userInterface.addToConsoleLog(sub);
                break;
            case 'e':
                userInterface.addToConsoleErr(sub);
                break;
        }
    }
}