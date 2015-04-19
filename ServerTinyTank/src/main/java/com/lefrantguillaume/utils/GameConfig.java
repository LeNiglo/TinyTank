package com.lefrantguillaume.utils;

import com.lefrantguillaume.game.Map;
import com.lefrantguillaume.game.enums.eGameMode;

/**
 * Created by Styve on 19/03/2015.
 */
public class GameConfig {
    private int ptsLimit;
    private int timeLimit;
    private Map map;
    private eGameMode gameMode;

    public GameConfig() {}

    public GameConfig(int ptsLimit, int timeLimit, Map map, eGameMode gameMode) {
        this.ptsLimit = ptsLimit;
        this.timeLimit = timeLimit;
        this.gameMode = gameMode;
    }

    public int getPtsLimit() {return ptsLimit;}
    public int getTimeLimit() {return timeLimit;}
    public eGameMode getGameMode() {return gameMode;}

    public void setPtsLimit(int ptsLimit) {this.ptsLimit = ptsLimit;}
    public void setTimeLimit(int timeLimit) {this.timeLimit = timeLimit;}
    public void setGameMode(eGameMode gameMode) {this.gameMode = gameMode;}
}
