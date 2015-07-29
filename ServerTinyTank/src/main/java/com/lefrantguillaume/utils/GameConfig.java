package com.lefrantguillaume.utils;

import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.gameComponent.gameMode.EnumGameMode;

/**
 * Created by Styve on 19/03/2015.
 */
public class GameConfig {
    public static int ptsLimit;
    public static int timeLimit;
    public static Map map;
    public static EnumGameMode gameMode;

    public GameConfig(int ptsLimit, int timeLimit, EnumGameMode gameMode) {
        GameConfig.ptsLimit = ptsLimit;
        GameConfig.timeLimit = timeLimit;
        GameConfig.gameMode = gameMode;
    }

    public EnumGameMode getGameMode() {
        return gameMode;
    }
}
