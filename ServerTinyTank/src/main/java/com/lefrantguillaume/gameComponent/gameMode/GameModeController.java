package com.lefrantguillaume.gameComponent.gameMode;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.modes.*;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.utils.GameConfig;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameModeController{
    private HashMap<EnumGameMode, GameMode> gameModes;
    private EnumGameMode currentGameMode;

    public GameModeController(ObstacleConfigData obstacleConfigData){
        this.gameModes = new HashMap();
        this.currentGameMode = GameConfig.gameMode == null ? EnumGameMode.TouchDown : GameConfig.gameMode;
        this.initGameModes(obstacleConfigData);
    }

    // FUNCTIONS
    public Object doTask(Pair<EnumAction, Object> task, Object data) {
        return this.getCurrentMode().doTask(task, data);
    }

    private void initGameModes(ObstacleConfigData obstacleConfigData){
        gameModes.put(EnumGameMode.FreeForAll, new FreeForAll(4));
        gameModes.put(EnumGameMode.TeamDeathMatch, new TeamDeathMatch(2));
        gameModes.put(EnumGameMode.Kingdom, new Kingdom(2, obstacleConfigData));
        gameModes.put(EnumGameMode.TouchDown, new TouchDown(2, obstacleConfigData));
    }


    // GETTERS
    public GameMode getCurrentMode(){
        return this.gameModes.get(this.currentGameMode);
    }

    public EnumGameMode getCurrentGameMode(){
        return this.currentGameMode;
    }

    public String isWinnerTeam(){
        return this.getCurrentMode().isWinnerTeam();
    }

    public boolean isPlayable(){
        return this.getCurrentMode().isPlayable();
    }

    // SETTERS
    public void setCurrentGameMode(EnumGameMode index){
        if (this.gameModes.containsKey(index)) {
            this.getCurrentMode().restartTeams();
            this.currentGameMode = index;
        }
    }

}
