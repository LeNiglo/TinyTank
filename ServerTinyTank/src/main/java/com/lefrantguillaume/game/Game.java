package com.lefrantguillaume.game;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.utils.GameConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Styve on 10/03/2015.
 */
public class Game extends Observable {
    private GameConfig config;
    private List<String> playerNames = new ArrayList<String>();

    public Game(GameConfig config) {
        this.config = config;
    }

    public void start() {
        this.setChanged();
        this.notifyObservers(true);
        WindowController.addConsoleMsg("Game started");
    }

    public List<String> getPlayerNames() {return playerNames;}
}
