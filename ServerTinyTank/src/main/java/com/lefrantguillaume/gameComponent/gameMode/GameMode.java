package com.lefrantguillaume.gameComponent.gameMode;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameMode{
    protected List<Team> teams;
    protected int maxPlayer;

    // FUNCTIONS
    public void init(int maxPlayer) {
        this.maxPlayer = maxPlayer;
        this.teams = new ArrayList<>();
        for (int i = 0; i < this.maxPlayer; ++i) {
            this.teams.add(new Team(UUID.randomUUID()));
        }
    }

    public UUID isWinnerTeam(){
        return null;
    }

    public void restart(){
        for (int i = 0; i < this.teams.size(); ++i){
            this.teams.get(i).init();
        }
    }

    // SETTERS
    public void setMaxPlayer(int maxPlayer) {
        if (maxPlayer < this.maxPlayer) {
            for (int i = this.teams.size(); i > maxPlayer; --i) {
                this.teams.remove(i);
            }
        } else {
            for (int i = this.teams.size(); i < maxPlayer; ++i) {
                this.teams.add(new Team(UUID.randomUUID()));
            }

        }
        this.maxPlayer = maxPlayer;
    }

    // GETTERS
    public int getMaxPlayer(){
        return this.maxPlayer;
    }

    public void doTask(Pair<EnumAction, Object> task) {
    }
}
