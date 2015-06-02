package com.lefrantguillaume.gameComponent.gameMode;

import com.lefrantguillaume.gameComponent.gameMode.modes.TeamDeathMatch;
import com.lefrantguillaume.gameComponent.gameMode.modes.FreeForAll;
import com.lefrantguillaume.gameComponent.gameMode.modes.GameMode;
import javafx.util.Pair;

import java.util.*;
import java.util.List;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameModeController{
    private List<GameMode> gameModes;
    private EnumGameMode currentGameMode;

    public GameModeController(){
        this.gameModes = new ArrayList<>();
        this.currentGameMode = EnumGameMode.TeamDeathMatch;
        this.initGameModes();
    }

    // FUNCTIONS
    public void doTask(Pair<EnumAction, Object> task) {
        this.getCurrentMode().doTask(task);
    }

    private void initGameModes(){
        gameModes.add(new FreeForAll(8));
        gameModes.add(new TeamDeathMatch(2));
    }


    // GETTERS
    public GameMode getCurrentMode(){
        return this.gameModes.get(this.currentGameMode.getIndex());
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
        this.getCurrentMode().restart();
        this.currentGameMode = index;
    }

}
