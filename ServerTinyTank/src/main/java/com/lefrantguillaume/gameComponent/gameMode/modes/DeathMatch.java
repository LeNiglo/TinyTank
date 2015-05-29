package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import javafx.util.Pair;

import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public class DeathMatch extends GameMode {

    public DeathMatch(int maxTeam) {
        this.objectiveScore = 50;
        this.maxPlayerTeam = 4;
        this.init(maxTeam, maxPlayerTeam);
    }

    // FUNCTIONS

    @Override
    public void doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.KILL)) {
            UUID teamId = (UUID) task.getValue();
            this.incrementScore(teamId, 10);
        }
    }

}
