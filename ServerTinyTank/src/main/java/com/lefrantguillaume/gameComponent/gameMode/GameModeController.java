package com.lefrantguillaume.gameComponent.gameMode;

import com.lefrantguillaume.gameComponent.gameMode.modes.FreeForAll;
import com.lefrantguillaume.gameComponent.gameMode.modes.GameMode;
import javafx.util.Pair;

import java.awt.*;
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
        this.currentGameMode = EnumGameMode.FreeForAll;
        this.initGameModes();
    }

    // FUNCTIONS
    public void doTask(Pair<EnumAction, Object> task) {
        this.getCurrentMode().doTask(task);
    }

    private void initGameModes(){
        gameModes.add(new FreeForAll(new Rectangle(0, 0, 1280, 768), 8));
    }


    // GETTERS
    public GameMode getCurrentMode(){
        return this.gameModes.get(this.currentGameMode.getIndex());
    }

    public EnumGameMode getCurrentGameMode(){
        return this.currentGameMode;
    }

    public UUID isWinnerTeam(){
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
