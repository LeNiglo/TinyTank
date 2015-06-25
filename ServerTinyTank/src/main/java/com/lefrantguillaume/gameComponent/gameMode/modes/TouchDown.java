package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.target.Targets;
import com.lefrantguillaume.networkComponent.dataServerComponent.DataServer;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessagePutObstacle;
import com.lefrantguillaume.utils.WindowConfig;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

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
    public Object doTask(Pair<EnumAction, Object> task, Object data) {

        if (task.getKey().equals(EnumAction.IN) && data instanceof Targets) {
            Pair<Player, Obstacle> values = (Pair<Player, Obstacle>) task.getValue();
            List<MessageModel> messages = new ArrayList<>();
            Targets targets = (Targets) data;

            WindowController.addConsoleMsg("IN AREA");
            if (values.getValue().getType() == EnumGameObject.BOMB_AREA) {
                WindowController.addConsoleMsg("GET OBJECTIVE");
                values.getKey().setTransportObjective(true);
                messages.add(targets.deleteObstacle(values.getValue().getId()));
            } else if (values.getValue().getType() == EnumGameObject.OBJECTIVE_AREA && !values.getValue().getPlayerId().equals(values.getKey().getId())) {
                if (values.getKey().isTransportObjective()) {
                    values.getKey().setTransportObjective(false);
                    WindowController.addConsoleMsg("GET POINTS");
                    this.incrementScore(values.getKey().getTeamId(), 1);
                    targets.addObstacle(this.obstacles.get(0));
                    Obstacle tmp = this.obstacles.get(0);
                    MessagePutObstacle message = new MessagePutObstacle(tmp.getPlayerId(), tmp.getPlayerPseudo(), tmp.getId(), tmp.getType(), tmp.getX(), tmp.getY(), tmp.getAngle());
                    messages.add(message);
                }
            }
            return messages;
        }
        return false;
    }

    @Override
    public void initObstacles(ObstacleConfigData obstacleConfigData) {
        Obstacle obstacle1 = obstacleConfigData.getObstacle(EnumGameObject.BOMB_AREA);
        obstacle1.createObstacle(DataServer.getId(), "admin", EnumGameObject.BOMB_AREA.getValue(), 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2);
        this.obstacles.add(obstacle1);
        Obstacle obstacle2 = obstacleConfigData.getObstacle(EnumGameObject.OBJECTIVE_AREA);
        obstacle2.createObstacle(this.teams.get(0).getId(), "admin", EnumGameObject.OBJECTIVE_AREA.getValue() + "1", 0, 50, 50);
        this.obstacles.add(obstacle2);
        Obstacle obstacle3 = obstacleConfigData.getObstacle(EnumGameObject.OBJECTIVE_AREA);
        obstacle3.createObstacle(this.teams.get(1).getId(), "admin", EnumGameObject.OBJECTIVE_AREA.getValue() + "2", 0, WindowConfig.getSizeX() - 50, WindowConfig.getSizeY() - 50);
        this.obstacles.add(obstacle3);

    }
}
