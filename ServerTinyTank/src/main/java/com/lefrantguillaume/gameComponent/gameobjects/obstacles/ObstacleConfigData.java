package com.lefrantguillaume.gameComponent.gameobjects.obstacles;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 01/05/2015.
 */
public class ObstacleConfigData {
    List<Obstacle> obstacles;
    private boolean valid;

    public ObstacleConfigData(){
        this.obstacles = new ArrayList<>();
        this.valid = false;
    }

    // FUNCTIONS
    public void initObstacles(JSONObject config) throws JSONException {

        /*
        JSONArray obstacleArray = config.getJSONArray("obstacles");

        for (int i = 0; i < obstacleArray.length(); ++i) {
                    }
        */
        obstacles.add(new Obstacle(EnumGameObject.IRON_WALL, new Pair<>(50f, 45f), new Pair<>(-25f, -22.5f), 20, 0));
        obstacles.add(new Obstacle(EnumGameObject.MINE, new Pair<>(50f, 45f), new Pair<>(-25f, -22.5f), 10, 10));
        this.valid = true;
    }
    public Obstacle getNewObstacle(int index){
        if (valid == true && index < this.obstacles.size()){
            return new Obstacle(this.obstacles.get(index));
        }
        return null;
    }

    public boolean isValid(){
        return this.valid;
    }
}
