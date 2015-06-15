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
public class TouchDown extends GameMode {

    public TouchDown(int maxTeam, ObstacleConfigData obstacleConfigData) {
        this.obstacles = new ArrayList<>();
        this.objectiveScore = 3;
        this.maxPlayerTeam = 4;
        this.init(maxTeam, maxPlayerTeam);
        this.initObstacles(obstacleConfigData);
    }

    // FUNCTIONS

    @Override
    public void doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.IN_BASE)) {
            String teamId = (String) task.getValue();
            this.incrementScore(teamId, 1);
        }
    }

    @Override
    public void initObstacles(ObstacleConfigData obstacleConfigData){
        Obstacle obstacle1 = obstacleConfigData.getObstacle(EnumGameObject.TOUCHDOWN_AREA);
        obstacle1.createObstacle(DataServer.getId(), "admin", EnumGameObject.TOUCHDOWN_AREA.getValue() + "1", 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2);
        this.obstacles.add(obstacle1);
        Obstacle obstacle2 = obstacleConfigData.getObstacle(EnumGameObject.TOUCHDOWN_AREA);
        obstacle2.createObstacle(DataServer.getId(), "admin", EnumGameObject.TOUCHDOWN_AREA.getValue() + "2", 0, 20, 20);
        this.obstacles.add(obstacle2);
        Obstacle obstacle3 = obstacleConfigData.getObstacle(EnumGameObject.TOUCHDOWN_AREA);
        obstacle3.createObstacle(DataServer.getId(), "admin", EnumGameObject.TOUCHDOWN_AREA.getValue() + "3", 0, WindowConfig.getSizeX() - 20, WindowConfig.getSizeY() - 20);
        this.obstacles.add(obstacle3);

    }
}
