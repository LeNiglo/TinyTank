package com.lefrantguillaume.gameComponent.gameobjects.obstacles;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.networkComponent.dataServerComponent.DataServer;
import com.lefrantguillaume.utils.WindowConfig;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;

/**
 * Created by andres_k on 09/06/2015.
 */
public class ObstacleConfigData {
    private HashMap<EnumGameObject, Obstacle> obstacles;
    private List<Obstacle> worldWall;

    public ObstacleConfigData(JSONObject obstacle) throws JSONException {
        this.obstacles = new HashMap<>();
        this.worldWall = new ArrayList<>();
        this.initObstacles(obstacle);
        this.createWorldWall();
    }

    private void initObstacles(JSONObject obstacles) throws JSONException {
        Iterator iterator = obstacles.keys();
        while (iterator.hasNext()) {
            String value = (String) iterator.next();
            JSONObject obstacle = obstacles.getJSONObject(value);
            Iterator iterator2 = obstacle.keys();
            while (iterator2.hasNext()) {
                String typeObstacle = (String) iterator2.next();
                JSONObject obj = obstacle.getJSONObject(typeObstacle);
                EnumGameObject type = EnumGameObject.getEnumByValue(typeObstacle);
                this.obstacles.put(type, ObstacleFactory.createBox(type, obj));
            }
        }
    }

    public Obstacle getObstacle(EnumGameObject type) {
        if (this.obstacles.containsKey(type)) {
            return new Obstacle(this.obstacles.get(type), false);
        } else {
            return null;
        }
    }

    public void createWorldWall(){
        Float sizeX = WindowConfig.getSizeX();
        Float sizeY = WindowConfig.getSizeY();

        WindowController.addConsoleMsg("map : [" + sizeX + ", " + sizeY + "]");
        Pair<Float, Float> pos1 = new Pair<>(sizeX / 2, 0f);
        Pair<Float, Float> pos2 = new Pair<>(sizeX / 2, sizeY);
        Pair<Float, Float> pos3 = new Pair<>(0f, sizeY / 2);
        Pair<Float, Float> pos4 = new Pair<>(sizeX, sizeY / 2);
        Pair<Float, Float> size1 = new Pair<>(sizeX, 10f);
        Pair<Float, Float> size2 = new Pair<>(10f, sizeY);
        Pair<Float, Float> origin1 = new Pair<>(-sizeX / 2, -10f);
        Pair<Float, Float> origin2 = new Pair<>(-sizeX / 2, 0f);
        Pair<Float, Float> origin3 = new Pair<>(-10f, -sizeY / 2);
        Pair<Float, Float> origin4 = new Pair<>(0f, -sizeY / 2);

        List<Block> block1 = new ArrayList<>();
        block1.add(new Block(origin1, size1));
        Obstacle obstacle1 = new Obstacle(EnumGameObject.UNBREAKABLE, block1, origin1, 1, 0);
        obstacle1.createObstacle(DataServer.getId(), "admin", "admin-UNBREAKABLE-Wall-1", 0, pos1.getKey(), pos1.getValue());
        this.worldWall.add(obstacle1);

        List<Block> block2 = new ArrayList<>();
        block2.add(new Block(origin2, size1));
        Obstacle obstacle2 = new Obstacle(EnumGameObject.UNBREAKABLE, block2, origin2, 1, 0);
        obstacle2.createObstacle(DataServer.getId(), "admin", "admin-UNBREAKABLE-Wall-2", 0, pos2.getKey(), pos2.getValue());
        this.worldWall.add(obstacle2);

        List<Block> block3 = new ArrayList<>();
        block3.add(new Block(origin3, size2));
        Obstacle obstacle3 = new Obstacle(EnumGameObject.UNBREAKABLE, block3, origin3, 1, 0);
        obstacle3.createObstacle(DataServer.getId(), "admin", "admin-UNBREAKABLE-Wall-3", 0, pos3.getKey(), pos3.getValue());
        this.worldWall.add(obstacle3);

        List<Block> block4 = new ArrayList<>();
        block4.add(new Block(origin4, size2));
        Obstacle obstacle4 = new Obstacle(EnumGameObject.UNBREAKABLE, block4, origin4, 1, 0);
        obstacle4.createObstacle(DataServer.getId(), "admin", "admin-UNBREAKABLE-Wall-4", 0, pos4.getKey(), pos4.getValue());
        this.worldWall.add(obstacle4);
    }

    public Obstacle getWorldWall(String id){
       for (int i = 0; i < this.worldWall.size(); ++i){
           if (id.equals(this.worldWall.get(i).getId())){
               return this.worldWall.get(i);
           }
       }
        return null;
    }
}
