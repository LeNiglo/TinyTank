package com.lefrantguillaume.utils;

import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.gameComponent.enums.EnumGameMode;

/**
 * Created by Styve on 19/03/2015.
 */
public class GameConfig {
    private int ptsLimit;
    private int timeLimit;
    private Map map;
    private EnumGameMode gameMode;

    public GameConfig() {}

    public GameConfig(int ptsLimit, int timeLimit, EnumGameMode gameMode) {
        this.ptsLimit = ptsLimit;
        this.timeLimit = timeLimit;
        this.gameMode = gameMode;
    }

    public int getPtsLimit() {return ptsLimit;}
    public int getTimeLimit() {return timeLimit;}
    public EnumGameMode getGameMode() {return gameMode;}
    public Map getMap() { return map; }

    public void setPtsLimit(int ptsLimit) {this.ptsLimit = ptsLimit;}
    public void setTimeLimit(int timeLimit) {this.timeLimit = timeLimit;}
    public void setGameMode(EnumGameMode gameMode) {this.gameMode = gameMode;}
    public void setMap(Map map) {this.map = map;}
}
