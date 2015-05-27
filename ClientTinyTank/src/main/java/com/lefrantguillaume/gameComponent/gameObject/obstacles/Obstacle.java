package com.lefrantguillaume.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.gameComponent.animations.Animator;

import java.util.UUID;

/**
 * Created by andres_k on 16/03/2015.
 */
public class Obstacle {
    private String userId;
    private String id;
    private Animator animator;
    private EnumObstacles type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private Pair<Float, Float> shiftOrigin;
    private float angle;
    private boolean created;

    public Obstacle(Animator animator, EnumObstacles type, Pair<Float, Float> size, Pair<Float, Float> shiftOrigin) {
        this.animator = animator;
        this.type = type;
        this.shiftOrigin = new Pair<Float, Float>(shiftOrigin);
        this.sizes = new Pair<Float, Float>(size);
        this.created = false;
    }

    public Obstacle(Obstacle obstacle) {
        this.animator = new Animator(obstacle.animator);
        this.type = obstacle.type;
        this.shiftOrigin = new Pair<Float, Float>(obstacle.shiftOrigin);
        this.sizes = new Pair<Float, Float>(obstacle.sizes);
        this.created = obstacle.created;
    }

    public void createObstacle(String userId, String id, float angle, float posX, float posY){
        this.userId = userId;
        this.angle = angle;
        this.id = id;
        this.positions = new Pair<Float, Float>(posX, posY);
        this.created = true;
    }

    // GETTERS
    public String getUserId() {
        return this.userId;
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public String getId() {
        return id;
    }

    public Animator getAnimator() {
        return animator;
    }

    public EnumObstacles getType() {
        return type;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getSizes() {
        return this.sizes;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return shiftOrigin;
    }

    public float getAngle() {
        return this.angle;
    }

    // SETTERS
    public void setX(float x) {
        this.positions.setV1(x);
    }

    public void setY(float y) {
        this.positions.setV2(y);
    }

    public void setSizeX(float x) {
        this.sizes.setV1(x);
    }

    public void setSizeY(float y) {
        this.sizes.setV2(y);
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
