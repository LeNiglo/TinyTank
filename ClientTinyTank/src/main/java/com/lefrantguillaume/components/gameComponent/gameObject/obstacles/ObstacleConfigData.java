package com.lefrantguillaume.components.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andres_k on 09/06/2015.
 */
public class ObstacleConfigData {
    private HashMap<EnumGameObject, Obstacle> obstacles;
    private List<Obstacle> worldWall;
    private boolean valid;

    public ObstacleConfigData() {
        this.obstacles = new HashMap<>();
        this.worldWall = new ArrayList<>();
        this.valid = false;
        this.createWorldWall();
    }

    public void initObstacle(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        Iterator iterator = config.keys();
        while (iterator.hasNext()) {
            String value = (String) iterator.next();
            JSONObject obj = config.getJSONObject(value);
            Iterator iterator2 = obj.keys();
            while (iterator2.hasNext()) {
                String typeObstacle = (String) iterator2.next();
                JSONObject obstacle = obj.getJSONObject(typeObstacle);
                EnumGameObject type = EnumGameObject.getEnumByValue(typeObstacle);
                this.obstacles.put(type, ObstacleFactory.createBox(animatorGameData, type, obstacle));
            }
        }
        this.valid = true;
    }

    public Obstacle getObstacle(EnumGameObject type) {
        if (valid == true && this.obstacles.containsKey(type)) {
            return new Obstacle(this.obstacles.get(type), false);
        } else {
            return null;
        }
    }
    public void createWorldWall(){
        Float sizeX = WindowConfig.getSizeX();
        Float sizeY = WindowConfig.getSizeY();

        Pair<Float, Float> size1 = new Pair<>(sizeX, 10f);
        Pair<Float, Float> size2 = new Pair<>(10f, sizeY);
        Pair<Float, Float> origin1 = new Pair<>(-sizeX / 2, -10f);
        Pair<Float, Float> origin2 = new Pair<>(-sizeX / 2, 0f);
        Pair<Float, Float> origin3 = new Pair<>(-10f, -sizeY / 2);
        Pair<Float, Float> origin4 = new Pair<>(0f, -sizeY / 2);

        List<Block> block1 = new ArrayList<>();
        block1.add(new Block(origin1, size1));
        Obstacle obstacle1 = new Obstacle(null, EnumGameObject.UNBREAKABLE, block1, origin1, 1, 0);
        obstacle1.setId("admin-UNBREAKABLE-Wall-1");
        this.worldWall.add(obstacle1);

        List<Block> block2 = new ArrayList<>();
        block2.add(new Block(origin2, size1));
        Obstacle obstacle2 = new Obstacle(null, EnumGameObject.UNBREAKABLE, block2, origin2, 1, 0);
        obstacle2.setId("admin-UNBREAKABLE-Wall-2");
        this.worldWall.add(obstacle2);

        List<Block> block3 = new ArrayList<>();
        block3.add(new Block(origin3, size2));
        Obstacle obstacle3 = new Obstacle(null, EnumGameObject.UNBREAKABLE, block3, origin3, 1, 0);
        obstacle3.setId("admin-UNBREAKABLE-Wall-3");
        this.worldWall.add(obstacle3);

        List<Block> block4 = new ArrayList<>();
        block4.add(new Block(origin4, size2));
        Obstacle obstacle4 = new Obstacle(null, EnumGameObject.UNBREAKABLE, block4, origin4, 1, 0);
        obstacle4.setId("admin-UNBREAKABLE-Wall-4");
        this.worldWall.add(obstacle4);
    }

    public Obstacle getWorldWall(String id){
        for (int i = 0; i < this.worldWall.size(); ++i){
            Debug.debug(id + " =?" + this.worldWall.get(i).getId());
            if (id.equals(this.worldWall.get(i).getId())){
                return this.worldWall.get(i);
            }
        }
        return null;
    }
}

