package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 16/03/2015.
 */
public class MapController {
    private List<Obstacle> obstacles;
    private CollisionController collisionController;
    private Pair<Float, Float> sizes;
    private Animator mapAnimator;
    private String configMapFile;

    public MapController(CollisionController collisionController, String configMapFile) throws SlickException {
        this.obstacles = new ArrayList<>();
        this.collisionController = collisionController;
        this.configMapFile = configMapFile;
        this.initMap();
        this.initObstacles();
    }

    // FUNCTIONS
    public void initMap() {
        sizes = new Pair<>(1280f, 768f);

    }

    public void initObstacles() {

    }

    public void addObstacle(Obstacle obstacle) {
        List<Block> block = obstacle.getCollisionObject();
        for (int i = 0; i < block.size(); ++i) {
            CollisionObject collisionObject = new CollisionObject(obstacle.getIgnoredObjectList(), obstacle.getPositions(), block.get(i).getSizes(),
                    block.get(i).getShiftOrigin(), obstacle.getId(), obstacle.getId(), obstacle.getType(), obstacle.getAngle());
            Debug.debug("addCollision obstacle: pos["  + collisionObject.getX() + "," + collisionObject.getY() + "] ; origin["
                    + collisionObject.getOriginX() + ","  + collisionObject.getOriginY() + "] size[" + collisionObject.getSizeX() + "," + collisionObject.getSizeY()+"]");
            obstacle.addObserver(collisionObject);
            this.collisionController.addCollisionObject(collisionObject);
        }
        this.obstacles.add(obstacle);
    }

    public void deleteObstacle(String id) {
        for (int i = 0; i < this.obstacles.size(); ++i) {
            if (this.obstacles.get(i).getId().equals(id)) {
                this.obstacles.remove(i);
                break;
            }
        }
        this.collisionController.deleteCollisionObject(id);
    }

    public void clearObstacles() {
        this.obstacles.clear();
    }

    // GETTERS
    public float getSizeX() {
        return this.sizes.getV1();
    }

    public float getSizeY() {
        return this.sizes.getV2();
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public Animator getMapAnimator() {
        return mapAnimator;
    }

    public String getConfigMapFile() {
        return configMapFile;
    }

    public Obstacle getObstacle(String id){
        for (Obstacle obstacle : this.obstacles) {
            if (id.equals(obstacle.getId())) {
                return obstacle;
            }
        }
        return null;
    }
    // SETTERS
    public void setMapAnimator(Animator mapAnimator) {
        this.mapAnimator = mapAnimator;
    }
}
