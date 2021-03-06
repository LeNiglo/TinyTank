package com.lefrantguillaume.components.gameComponent.RoundData;

import com.lefrantguillaume.components.gameComponent.playerData.data.Player;

import java.util.List;

/**
 * Created by andres_k on 16/03/2015.
 */
public class RoundController {
    private List<Player> players;
    private List<Team> teams;
    private EnumGameMode gameMode;

    public RoundController(List<Player> players, List<Team> teams) {
        this.players = players;
        this.teams = teams;
        this.gameMode = EnumGameMode.NULL;
    }

    // GETTERS
    public EnumGameMode getGameMode() {
        return this.gameMode;
    }

    // SETTERS
    public void setGameMode(EnumGameMode gameMode) {
        this.gameMode = gameMode;
    }
}
