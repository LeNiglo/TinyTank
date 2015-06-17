package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.WindowController;
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
    public boolean doTask(Pair<EnumAction, Object> task) {

        if (task.getKey().equals(EnumAction.IN)) {
            Pair<String, String> values = (Pair<String, String>) task.getValue();

            WindowController.addConsoleMsg("IN AREA");
            Obstacle obstacle = this.getSpecificObstacle(values.getValue());
            if (obstacle != null) {
                if (obstacle.getType() == EnumGameObject.OBJECTIVE_AREA) {
                    if (this.canDoObjective == true) {
                        WindowController.addConsoleMsg("GET OBJECTIVE");
                        this.canDoObjective = false;
                        return true;
                    }
                } else if (obstacle.getType() == EnumGameObject.SPAWN_AREA && !obstacle.getPlayerId().equals(values.getKey())) {
                    if (this.canDoObjective == false) {
                        this.canDoObjective = true;
                        WindowController.addConsoleMsg("GET POINTS");
                        this.incrementScore(values.getKey(), 1);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void initObstacles(ObstacleConfigData obstacleConfigData){
        Obstacle obstacle1 = obstacleConfigData.getObstacle(EnumGameObject.OBJECTIVE_AREA);
        obstacle1.createObstacle(DataServer.getId(), "admin", EnumGameObject.OBJECTIVE_AREA.getValue(), 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2);
        this.obstacles.add(obstacle1);
        Obstacle obstacle2 = obstacleConfigData.getObstacle(EnumGameObject.SPAWN_AREA);
        obstacle2.createObstacle(this.teams.get(0).getId(), "admin", EnumGameObject.SPAWN_AREA.getValue() + "1", 0, 50, 50);
        this.obstacles.add(obstacle2);
        Obstacle obstacle3 = obstacleConfigData.getObstacle(EnumGameObject.SPAWN_AREA);
        obstacle3.createObstacle(this.teams.get(1).getId(), "admin", EnumGameObject.SPAWN_AREA.getValue() + "2", 0, WindowConfig.getSizeX() - 50, WindowConfig.getSizeY() - 50);
        this.obstacles.add(obstacle3);

    }
}
