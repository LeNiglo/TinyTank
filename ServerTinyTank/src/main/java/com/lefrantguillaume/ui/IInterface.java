package com.lefrantguillaume.ui;

import com.lefrantguillaume.utils.GameConfig;


/**
 * Created by Styve on 08/05/2015.
 */
public interface IInterface {
    public abstract void addToConsoleLog(String msg);
    public abstract void clearMapList();
    public abstract void addMap(String mapName);
    public abstract void tellNoMap();
    public abstract int getSelectedMapIndex();
    public abstract GameConfig getGameConfig();
    public abstract void gameStarted();
    public abstract void gameStopped();
    public abstract void addPlayer(String pseudo);
    public abstract void delPlayer(String pseudo);
    public abstract void clearPlayerList();
}
