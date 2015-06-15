package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.Team;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameMode {
    protected List<Team> teams;
    protected List<Obstacle> obstacles;
    protected int maxPlayerTeam;
    protected int objectiveScore;
    protected boolean playable;
    protected boolean canDoObjective;

    // FUNCTIONS
    public void init(int maxTeam, int maxPlayer) {
        this.playable = true;
        this.canDoObjective = true;
        this.maxPlayerTeam = maxPlayer;
        this.teams = new ArrayList<>();
        for (int i = 0; i < maxTeam; ++i) {
            this.teams.add(new Team(UUID.randomUUID().toString()));
        }
    }

    public void initObstacles(ObstacleConfigData obstacleConfigData){
    }

    public void restart() {
        for (int i = 0; i < this.teams.size(); ++i) {
            this.teams.get(i).init();
        }
    }

    public void start() {
        this.restart();
        this.playable = true;
    }

    public void stop() {
        this.playable = false;
    }

    public boolean doTask(Pair<EnumAction, Object> task) {
        return false;
    }

    public String attributeATeam() {
        String idTeam = null;
        int lastNumber = 0;

        if (this.countOpenSlot() != 0) {
            for (int i = 0; i < this.teams.size(); ++i) {
                if (idTeam == null || lastNumber > this.teams.get(i).getCurrentPlayers()) {
                    idTeam = this.teams.get(i).getId();
                    lastNumber = this.teams.get(i).getCurrentPlayers();
                }
            }
        }
        return idTeam;
    }

    public void changePlayerInTeam(String idTeam, int value){
        for (int i = 0; i < this.teams.size(); ++i){
            if (idTeam.equals(this.teams.get(i).getId())) {
                this.teams.get(i).changeCurrentPlayers(value);
            }
        }
    }
    protected void incrementScore(String teamId, int value) {
        for (int i = 0; i < this.teams.size(); ++i) {
            if (this.teams.get(i).getId().equals(teamId)) {
                this.teams.get(i).addToCurrentScore(value);
            }
        }
    }

    public int countOpenSlot() {
        int currentPlayer = 0;
        for (int i = 0; i < this.teams.size(); ++i) {
            currentPlayer += this.teams.get(i).getCurrentPlayers();
        }
        return this.maxPlayerTeam - currentPlayer;
    }

    // SETTERS
    public void setMaxTeam(int maxTeam) {
        if (maxTeam < this.teams.size()) {
            for (int i = this.teams.size(); i > maxTeam; --i) {
                this.teams.remove(i);
            }
        } else {
            for (int i = this.teams.size(); i < maxTeam; ++i) {
                this.teams.add(new Team(UUID.randomUUID().toString()));
            }

        }
    }

    public void setCanDoObjective(boolean value){
        this.canDoObjective = value;
    }

    // GETTERS

    public boolean isCanDoObjective(){
        return this.canDoObjective;
    }

    public int getObjectiveScore() {
        return this.objectiveScore;
    }

    public int getMaxTeam() {
        return this.teams.size();
    }

    public int getMaxPlayerTeam() {
        return this.maxPlayerTeam;
    }

    public String isWinnerTeam() {
        String teamId = null;

        for (int i = 0; i < this.teams.size(); ++i) {
            Team current = this.teams.get(i);
            if (current.getCurrentScore() >= this.getObjectiveScore()) {
                teamId = current.getId();
            }
        }
        return teamId;
    }

    public List<Obstacle> getObstacles(){
        return this.obstacles;
    }

    public Obstacle getSpecificObstacle(String id){
        for (Obstacle obstacle : this.obstacles){
            if (obstacle.getId().equals(id))
                return obstacle;
        }
        return null;
    }

    public boolean isPlayable() {
        return this.playable;
    }

    public int getIndexTeam(String idTeam) {
        for (int i = 0; i < this.teams.size(); ++i) {
            if (idTeam.equals(this.teams.get(i).getId())) {
                return i + 1;
            }
        }
        return 0;
    }
}
