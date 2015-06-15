package com.lefrantguillaume.gameComponent.gameMode;

import com.lefrantguillaume.gameComponent.gameMode.modes.*;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameModeController{
    private HashMap<EnumGameMode, GameMode> gameModes;
    private EnumGameMode currentGameMode;

    public GameModeController(){
        this.gameModes = new HashMap();
        this.currentGameMode = EnumGameMode.TeamDeathMatch;
        this.initGameModes();
    }

    // FUNCTIONS
    public void doTask(Pair<EnumAction, Object> task) {
        this.getCurrentMode().doTask(task);
    }

    private void initGameModes(){
        gameModes.put(EnumGameMode.FreeForAll, new FreeForAll(8));
        gameModes.put(EnumGameMode.TeamDeathMatch, new TeamDeathMatch(2));
        gameModes.put(EnumGameMode.Kingdom, new Kingdom(2));
        gameModes.put(EnumGameMode.TouchDown, new TouchDown(2));
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
            this.getCurrentMode().restart();
            this.currentGameMode = index;
        }
    }

}
