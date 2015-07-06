package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

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
    public void createObstacles(ObstacleConfigData obstacleConfigData) {
    }

    @Override
    public Object doTask(Pair<EnumAction, Object> task, Object data) {
        List<MessageModel> messages = new ArrayList<>();

        if (task.getKey().equals(EnumAction.KILL)) {
            Player killer = (Player) task.getValue();
            Player target = (Player) data;

            messages.add(this.incrementScore(killer.getTeamId(), 10));

            killer.addKill();
            target.addDeath();
            messages.add(killer.addScore(10));
        }
        return messages;
    }
}
