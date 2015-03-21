package com.lefrantguillaume.game;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.network.MessageConnectData;
import com.lefrantguillaume.network.MessageDownloadData;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.utils.GameConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Styve on 10/03/2015.
 */
public class Game extends Observable implements Observer {
    private TinyServer server;
    private GameConfig config;
    private List<String> playerNames = new ArrayList<String>();

    public Game() {
        this.server = new TinyServer();
        this.server.addObserver(this);
    }

    public void start() {
        this.setChanged();
        this.notifyObservers(server.start());
        WindowController.addConsoleMsg("Game started");
    }

    public void stop() {
        server.stop();
        this.setChanged();
        this.notifyObservers("stop");
    }

    public List<String> getPlayerNames() {return playerNames;}

    public void update(Observable o, Object arg) {
        if (arg instanceof MessageDownloadData || arg instanceof MessageConnectData) {
            this.setChanged();
            this.notifyObservers(arg);
        }
    }

    public GameConfig getConfig() {return config;}
    public void setConfig(GameConfig config) {this.config = config;}
}
