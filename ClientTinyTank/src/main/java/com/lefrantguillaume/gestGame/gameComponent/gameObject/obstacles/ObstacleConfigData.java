package com.lefrantguillaume.gestGame.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.gestGame.Utils.stockage.Pair;
import com.lefrantguillaume.gestGame.gameComponent.animations.AnimatorGameData;
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
        this.obstacles = new ArrayList<Obstacle>();
        this.valid = false;
    }

    // FUNCTIONS
    public void initObstacles(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {

        /*
        JSONArray obstacleArray = config.getJSONArray("obstacles");

        for (int i = 0; i < obstacleArray.length(); ++i) {
                    }
        */
        obstacles.add(new Obstacle(animatorGameData.getObstacleAnimator(EnumObstacles.WALL_WOOD), EnumObstacles.WALL_WOOD, new Pair<Float, Float>(50f, 45f), new Pair<Float, Float>(25f, 22.5f)));
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
