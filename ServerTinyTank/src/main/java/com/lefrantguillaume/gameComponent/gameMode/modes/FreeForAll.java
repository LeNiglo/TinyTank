package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.GameMode;
import com.lefrantguillaume.gameComponent.gameMode.Team;
import javafx.util.Pair;

import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public class FreeForAll extends GameMode {
    private final int objectiveScore;

    public FreeForAll(int maxPlayer) {
        this.objectiveScore = 300;
        this.init(maxPlayer);
    }

    // FUNCTIONS

    private void incrementScore(UUID teamId, int value) {
        for (int i = 0; i < this.teams.size(); ++i) {
            if (this.teams.get(i).getId().equals(teamId)) {
                this.teams.get(i).addToCurrentScore(value);
            }
        }
    }

    @Override
    public void doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.KILL)) {
            UUID teamId = (UUID) task.getValue();
            this.incrementScore(teamId, 10);
        }
    }

    @Override
    public UUID isWinnerTeam() {
        UUID teamId = null;

        for (int i = 0; i < this.teams.size(); ++i){
            Team current = this.teams.get(i);
            if (current.getCurrentScore() == this.objectiveScore){
                return current.getId();
            }
        }
        return teamId;
    }

    // GETTERS
    public int getObjectiveScore() {
        return this.objectiveScore;
    }
}
