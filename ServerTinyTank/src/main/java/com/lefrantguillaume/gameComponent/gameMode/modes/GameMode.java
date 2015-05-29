package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.Team;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public class GameMode {
    protected List<Team> teams;
    protected int maxPlayerTeam;
    protected int objectiveScore;
    protected boolean playable;

    // FUNCTIONS
    public void init(int maxTeam, int maxPlayer) {
        this.playable = true;
        this.maxPlayerTeam = maxPlayer;
        this.teams = new ArrayList<>();
        for (int i = 0; i < maxTeam; ++i) {
            this.teams.add(new Team(UUID.randomUUID()));
        }
    }

    public void restart() {
        for (int i = 0; i < this.teams.size(); ++i) {
            this.teams.get(i).init();
        }
    }

    public void start() {
        this.playable = true;
        this.restart();
    }

    public void stop() {
        this.playable = false;
    }

    public void doTask(Pair<EnumAction, Object> task) {
    }

    public UUID attributeATeam() {
        UUID idTeam = null;
        int lastNumber = 0;

        if (this.openSlot() != 0) {
            for (int i = 0; i < this.teams.size(); ++i) {
                if (idTeam == null || lastNumber > this.teams.get(i).getCurrentPlayers()) {
                    idTeam = this.teams.get(i).getId();
                    lastNumber = this.teams.get(i).getCurrentPlayers();
                }
            }
        }
        return idTeam;
    }

    protected void incrementScore(UUID teamId, int value) {
        for (int i = 0; i < this.teams.size(); ++i) {
            if (this.teams.get(i).getId().equals(teamId)) {
                this.teams.get(i).addToCurrentScore(value);
            }
        }
    }

    public int openSlot() {
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
                this.teams.add(new Team(UUID.randomUUID()));
            }

        }
    }

    // GETTERS

    public int getObjectiveScore() {
        return this.objectiveScore;
    }

    public int getMaxTeam() {
        return this.teams.size();
    }

    public int getMaxPlayerTeam() {
        return this.maxPlayerTeam;
    }

    public UUID isWinnerTeam() {

        UUID teamId = null;

        for (int i = 0; i < this.teams.size(); ++i) {
            Team current = this.teams.get(i);
            if (current.getCurrentScore() == this.objectiveScore) {
                teamId = current.getId();
            }
        }
        return teamId;
    }

    public boolean isPlayable() {
        return this.playable;
    }

    public int getIndexTeam(String idTeam) {
        for (int i = 0; i < this.teams.size(); ++i) {
            if (idTeam.equals(this.teams.get(i).getId().toString())) {
                return i + 1;
            }
        }
        return 0;
    }
}
