package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.obstacles.Obstacle;
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
        this.obstacles = new ArrayList<Obstacle>();
        this.collisionController = collisionController;
        this.configMapFile = configMapFile;
        //tmp
        sizes = new Pair<Float, Float>(1280f, 768f);
    }

    // FUNCTIONS
    public void addObstacle(Obstacle obstacle){
        CollisionObject collisionObject = new CollisionObject(true, obstacle.getX(), obstacle.getY(), obstacle.getSizeX(), obstacle.getSizeY(), obstacle.getUserId(), obstacle.getId(), EnumType.OBSTACLE);
        this.obstacles.add(obstacle);
        this.collisionController.addCollisionObject(collisionObject);
    }

    public void deleteObstacle(int id){
        for (int i = 0; i < this.obstacles.size(); ++i){
            if (this.obstacles.get(i).getId() == id){
                this.obstacles.remove(i);
                break;
            }
        }
        this.collisionController.deleteCollisionObject(id);
    }

    // GETTERS
    public float getSizeX(){
        return this.sizes.getV1();
    }

    public float getSizeY(){
        return this.sizes.getV2();
    }

    public List<Obstacle> getObstacles(){return this.obstacles;}

    public Animator getMapAnimator() {
        return mapAnimator;
    }

    public String getConfigMapFile() {
        return configMapFile;
    }

    // SETTERS
    public void setMapAnimator(Animator mapAnimator) {
        this.mapAnimator = mapAnimator;
    }
}
