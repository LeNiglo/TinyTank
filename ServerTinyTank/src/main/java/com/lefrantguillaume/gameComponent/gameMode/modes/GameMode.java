package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.Team;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageRoundScore;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public abstract class GameMode {
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
            this.teams.add(new Team(UUID.randomUUID().toString(), "Team n°" + String.valueOf(i)));
        }
    }

    public abstract void createObstacles(ObstacleConfigData obstacleConfigData);

    public void startRound() {
        this.playable = true;
    }

    public void endRound(){
        this.playable = false;
    }

    public void initRound(){
        this.restartTeams();
        this.restartObstacles();
    }

    public void restartTeams() {
        for (int i = 0; i < this.teams.size(); ++i) {
            this.teams.get(i).init();
        }
    }

    public void restartObstacles(){
        for (Obstacle obstacle : this.obstacles){
            obstacle.setId(UUID.randomUUID().toString());
        }
    }

    public abstract Object doTask(Pair<EnumAction, Object> task, Object data);

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
    protected MessageModel incrementScore(String teamId, int value) {
        for (int i = 0; i < this.teams.size(); ++i) {
            if (this.teams.get(i).getId().equals(teamId)) {
                this.teams.get(i).addToCurrentScore(value);
                return new MessageRoundScore(this.teams.get(i).getName(), this.teams.get(i).getId(), this.teams.get(i).getId(), EnumGameObject.NULL, this.teams.get(i).getCurrentScore());
            }
        }
        return null;
    }

    public int countOpenSlot() {
        int openSlot = 0;
        for (int i = 0; i < this.teams.size(); ++i) {
            openSlot += (this.maxPlayerTeam - this.teams.get(i).getCurrentPlayers());
        }
        return openSlot;
    }

    // SETTERS
    public void setMaxTeam(int maxTeam) {
        if (maxTeam < this.teams.size()) {
            for (int i = this.teams.size(); i > maxTeam; --i) {
                this.teams.remove(i);
            }
        } else {
            for (int i = this.teams.size(); i < maxTeam; ++i) {
                this.teams.add(new Team(UUID.randomUUID().toString(), "Team n°" + String.valueOf(i)));
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

    public List<Team> getTeams() {
        return this.teams;
    }

    public int getMaxPlayerTeam() {
        return this.maxPlayerTeam;
    }

    public String isWinnerTeam() {
        String teamId = null;

        for (Team current : this.teams) {
            if (current.getCurrentScore() >= this.getObjectiveScore()) {
                teamId = current.getId();
            }
        }
        return teamId;
    }

    public List<Obstacle> getObstacles(){
        return this.obstacles;
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

    public String getTeam(int index){
        if (index < this.teams.size()) {
            return this.teams.get(index).getId();
        }
        return "";
    }
}
