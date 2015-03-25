package com.lefrantguillaume.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

import java.util.UUID;

/**
 * Created by andres_k on 16/03/2015.
 */
public class Obstacle {
    private String userId;
    private UUID id;
    private Animator animator;
    private EnumType type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private Pair<Float, Float> shiftOrigin;

    public Obstacle(float x, float y, float sizeX, float sizeY, String userId, Animator animator, EnumType type, Pair<Float, Float> shiftOrigin) {
        this.userId = userId;
        this.shiftOrigin = shiftOrigin;
        this.id = UUID.randomUUID();
        this.animator = animator;
        this.type = type;
        this.positions = new Pair<Float, Float>(x, y);
        this.sizes = new Pair<Float, Float>(sizeX, sizeY);
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

    public UUID getId() {
        return id;
    }

    public Animator getAnimator() {
        return animator;
    }

    public EnumType getType() {
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
