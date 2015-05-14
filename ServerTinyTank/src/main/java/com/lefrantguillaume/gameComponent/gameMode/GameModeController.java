package com.lefrantguillaume.gameComponent.gameMode;

import com.lefrantguillaume.gameComponent.enums.EnumGameMode;
import com.lefrantguillaume.gameComponent.gameMode.modes.FreeForAll;

import java.util.*;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameModeController implements Observer {
    private List<GameMode> gameModes;
    private EnumGameMode currentGameMode;

    public GameModeController(){
        this.gameModes = new ArrayList<>();
        this.currentGameMode = EnumGameMode.FreeForAll;
        this.initGameModes();
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {
        this.getCurrentMode().update(o, arg);
    }

    public UUID isWinnerTeam(){
        return this.getCurrentMode().isWinnerTeam();
    }

    private void initGameModes(){
        gameModes.add(new FreeForAll(8));
    }

    public void restartGameMode(){
        this.getCurrentMode().restart();
    }
    // GETTERS
    public GameMode getCurrentMode(){
        return this.gameModes.get(this.currentGameMode.getIndex());
    }

    // SETTERS
    public void setCurrentGameMode(EnumGameMode index){
        this.getCurrentMode().restart();
        this.currentGameMode = index;
    }

}
