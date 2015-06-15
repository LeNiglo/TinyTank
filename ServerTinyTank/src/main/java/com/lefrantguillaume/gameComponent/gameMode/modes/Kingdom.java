package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.networkComponent.dataServerComponent.DataServer;
import com.lefrantguillaume.utils.WindowConfig;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by andres_k on 13/05/2015.
 */
public class Kingdom extends GameMode {

    public Kingdom(int maxTeam, ObstacleConfigData obstacleConfigData) {
        this.obstacles = new ArrayList<>();
        this.objectiveScore = 100;
        this.maxPlayerTeam = 4;
        this.init(maxTeam, maxPlayerTeam);
        this.initObstacles(obstacleConfigData);
    }

    // FUNCTIONS

    @Override
    public boolean doTask(Pair<EnumAction, Object> task) {
        Pair<String, String> values = (Pair<String, String>) task.getValue();
        if (task.getKey().equals(EnumAction.KILL)) {
            this.incrementScore(values.getKey(), 5);
        } else if (task.getKey().equals(EnumAction.IN)) {
            this.incrementScore(values.getKey(), 10);
        }
        return false;
    }

    @Override
    public void initObstacles(ObstacleConfigData obstacleConfigData) {
        Obstacle obstacle = obstacleConfigData.getObstacle(EnumGameObject.OBJECTIVE_AREA);
        obstacle.createObstacle(DataServer.getId(), "admin", EnumGameObject.OBJECTIVE_AREA.getValue(), 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2);
        this.obstacles.add(obstacle);
    }
}
