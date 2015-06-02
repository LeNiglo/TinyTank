package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import javafx.util.Pair;

/**
 * Created by andres_k on 13/05/2015.
 */
public class FreeForAll extends GameMode {

    public FreeForAll(int maxTeam) {
        this.objectiveScore = 30;
        this.maxPlayerTeam = 1;
        this.init(maxTeam, maxPlayerTeam);
    }

    // FUNCTIONS

    @Override
    public void doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.KILL)) {
            String teamId = (String) task.getValue();
            this.incrementScore(teamId, 10);
        }
    }
}
