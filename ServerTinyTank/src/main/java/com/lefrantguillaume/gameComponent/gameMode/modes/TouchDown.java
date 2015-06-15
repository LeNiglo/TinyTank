package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import javafx.util.Pair;

/**
 * Created by andres_k on 13/05/2015.
 */
public class TouchDown extends GameMode {

    public TouchDown(int maxTeam) {
        this.objectiveScore = 3;
        this.maxPlayerTeam = 4;
        this.init(maxTeam, maxPlayerTeam);
    }

    // FUNCTIONS

    @Override
    public void doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.IN_BASE)) {
            String teamId = (String) task.getValue();
            this.incrementScore(teamId, 1);
        }
    }

}
