package com.lefrantguillaume.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

/**
 * Created by andres_k on 16/03/2015.
 */
public class Obstacle {
    private int userId;
    private int id;
    private Animator animator;
    private EnumType type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;

    public Obstacle(float x, float y, float sizeX, float sizeY, int userId, int id, Animator animator, EnumType type) {
        this.userId = userId;
        this.id = id;
        this.animator = animator;
        this.type = type;
        this.positions = new Pair<Float, Float>(x, y);
        this.sizes = new Pair<Float, Float>(sizeX, sizeY);
    }

    // GETTERS
    public int getUserId() {
        return this.userId;
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public float getSizeX() {
        return this.sizes.getV1();
    }

    public float getSizeY() {
        return this.sizes.getV2();
    }

    public int getId() {
        return id;
    }

    public Animator getAnimator() {
        return animator;
    }

    public EnumType getType() {
        return type;
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
}
