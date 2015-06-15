package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by andres_k on 13/05/2015.
 */
public class TeamDeathMatch extends GameMode {

    public TeamDeathMatch(int maxTeam) {
        this.obstacles = new ArrayList<>();
        this.objectiveScore = 50;
        this.maxPlayerTeam = 4;
        this.init(maxTeam, maxPlayerTeam);
    }

    // FUNCTIONS

    @Override
    public boolean doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.KILL)) {
            String teamId = (String) task.getValue();
            this.incrementScore(teamId, 10);
        }
        return false;
    }
}
