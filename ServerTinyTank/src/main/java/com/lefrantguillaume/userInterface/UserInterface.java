package com.lefrantguillaume.userInterface;


import com.lefrantguillaume.utils.GameConfig;

/**
 * Created by Styve on 08/05/2015.
 */
public interface UserInterface {
    public abstract void addToConsoleLog(String msg);

    public abstract void addToConsoleErr(String msg);

    public abstract void tellNoMap();

    public abstract int getSelectedMapIndex();

    public abstract void startGame();

    public abstract void stopGame();

    public abstract void refreshPlayers();

    public abstract void refreshMaps();

    public abstract GameConfig getGameConfig();
}
