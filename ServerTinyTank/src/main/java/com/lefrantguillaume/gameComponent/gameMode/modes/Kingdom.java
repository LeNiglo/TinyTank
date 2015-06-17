package com.lefrantguillaume.gameComponent.gameMode.modes;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.Team;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.networkComponent.dataServerComponent.DataServer;
import com.lefrantguillaume.utils.WindowConfig;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by andres_k on 13/05/2015.
 */
public class Kingdom extends GameMode {
    private HashMap<String, Integer> playersInObjective;
    private Timer timer;
    private myTask task;
    private boolean timerRunning;

    public Kingdom(int maxTeam, ObstacleConfigData obstacleConfigData) {
        this.obstacles = new ArrayList<>();
        this.playersInObjective = new HashMap<>();
        this.objectiveScore = 100;
        this.maxPlayerTeam = 4;
        this.init(maxTeam, maxPlayerTeam);
        this.initObstacles(obstacleConfigData);
        this.initPlayersInObjective();
        this.timerRunning = false;
        this.timer = new Timer(true);
    }

    // FUNCTIONS

    @Override
    public boolean doTask(Pair<EnumAction, Object> task) {
        Pair<String, String> values = (Pair<String, String>) task.getValue();
        if (task.getKey().equals(EnumAction.KILL)) {
            this.incrementScore(values.getKey(), 5);
        } else if (task.getKey().equals(EnumAction.IN)) {
            if (this.playersInObjective.containsKey(values.getKey())) {
                this.playersInObjective.replace(values.getKey(), this.playersInObjective.get(values.getKey()) + 1);
                if (this.timerRunning == false) {
                    this.startTimer();
                }
            }
        } else if (task.getKey().equals(EnumAction.OUT)) {
            if (this.playersInObjective.containsKey(values.getKey())) {
                int value = this.playersInObjective.get(values.getKey()) - 1;
                if (value < 0)
                    value = 0;
                this.playersInObjective.replace(values.getKey(), value);
            }
            if (this.getPlayersInObjective() == 0) {
                this.resumeTimer();
            }
        }
        return false;
    }

    @Override
    public void initObstacles(ObstacleConfigData obstacleConfigData) {
        Obstacle obstacle = obstacleConfigData.getObstacle(EnumGameObject.OBJECTIVE_AREA);
        obstacle.createObstacle(DataServer.getId(), "admin", EnumGameObject.OBJECTIVE_AREA.getValue(), 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2);
        this.obstacles.add(obstacle);
    }

    private void initPlayersInObjective() {
        for (Team team : this.teams) {
            this.playersInObjective.put(team.getId(), 0);
        }
    }

    @Override
    public void restart() {
        for (int i = 0; i < this.teams.size(); ++i) {
            this.teams.get(i).init();
        }
        this.playersInObjective.clear();
        this.initPlayersInObjective();
        if (this.timerRunning == true) {
            this.resumeTimer();
        }
    }

    private void resumeTimer() {
        WindowController.addConsoleMsg("RESUME TIMER");
        this.task.cancel();
        this.timerRunning = false;
    }

    private void startTimer() {
        WindowController.addConsoleMsg("START TIMER");
        task = new myTask();
        this.timerRunning = true;
        this.timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // GETTERS

    private int getPlayersInObjective() {
        int number = 0;

        for (Map.Entry item : this.playersInObjective.entrySet()) {
            number += (Integer) item.getValue();
        }
        return number;
    }

    private String getTeamInObjective() {
        int number = 0;
        String idTeam = null;

        for (Map.Entry item : this.playersInObjective.entrySet()) {
            Integer value = (Integer) item.getValue();
            if (value != 0){
                if (number != 0)
                    return null;
                number = value;
                idTeam = (String) item.getKey();
            }
        }
        return idTeam;
    }

    private class myTask extends TimerTask{
        @Override
        public void run() {
            String idTeam = getTeamInObjective();
            WindowController.addConsoleMsg("team? " + idTeam);
            if (idTeam != null) {
                WindowController.addConsoleMsg("ADD POINTS, so winner ? " + isWinnerTeam());
                incrementScore(idTeam, 10);
            }
        }
    }
}
