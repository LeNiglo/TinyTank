package com.lefrantguillaume;

import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.utils.ServerConfig;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main {
    public static void main(String args[]) {
        if (!ServerConfig.loadConfig())
            ServerConfig.writeConfig();
        TinyServer server = new TinyServer();

        com.lefrantguillaume.ui.ServerConfig app = new com.lefrantguillaume.ui.ServerConfig(server);
        WindowObserver a = new WindowObserver(app);
        new WindowController(a);
    }
}

class WindowControllerObserver extends Observable {
    public void addMessage(String msg) {
        this.setChanged();
        this.notifyObservers(msg);
    }
}

class WindowObserver implements Observer {
    private final com.lefrantguillaume.ui.ServerConfig window;

    public WindowObserver(com.lefrantguillaume.ui.ServerConfig window) {
        this.window = window;
    }

    @Override
    public void update(Observable o, Object arg) {
        window.addToConsoleLog((String) arg);
    }
}