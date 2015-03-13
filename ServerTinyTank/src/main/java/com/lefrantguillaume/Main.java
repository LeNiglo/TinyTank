package com.lefrantguillaume;

import com.lefrantguillaume.game.Game;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.master.Master;
import com.lefrantguillaume.ui.ServerConfig;
import com.lefrantguillaume.utils.Configuration;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main {
    public static void main(String args[]) {
        if (!Configuration.loadConfig())
            Configuration.writeConfig();
        TinyServer server = new TinyServer();

        ServerConfig app = new ServerConfig(server);
        WindowObserver a = new WindowObserver(app);
        new WindowController(a);


        Master master = new Master();
        master.initServer();
        new Game();
    }
}

class WindowControllerObserver extends Observable {
    public void addMessage(String msg) {
        this.setChanged();
        this.notifyObservers(msg);
    }
}

class WindowObserver implements Observer {
    private final ServerConfig window;

    public WindowObserver(ServerConfig window) {
        this.window = window;
    }

    @Override
    public void update(Observable o, Object arg) {
        window.addToConsoleLog((String) arg);
    }
}