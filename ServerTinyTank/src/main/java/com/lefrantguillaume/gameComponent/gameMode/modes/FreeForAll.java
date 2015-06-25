package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by andres_k on 13/05/2015.
 */
public class FreeForAll extends GameMode {

    public FreeForAll(int maxTeam) {
        this.obstacles = new ArrayList<>();
        this.objectiveScore = 30;
        this.maxPlayerTeam = 1;
        this.init(maxTeam, maxPlayerTeam);
    }

    // FUNCTIONS

    @Override
    public Object doTask(Pair<EnumAction, Object> task, Object data) {

        if (task.getKey().equals(EnumAction.KILL)) {
            String teamId = (String) task.getValue();
            this.incrementScore(teamId, 10);
        }
        return false;
    }
}
