package com.lefrantguillaume.gameComponent.gameobjects.obstacles;


import javafx.util.Pair;

/**
 * Created by andres_k on 25/03/2015.
 */
public class Block {
    private final Pair<Float, Float> shiftOrigin;
    private final Pair<Float, Float> sizes;

    public Block(Pair<Float, Float> shiftOrigin, Pair<Float, Float> sizes) {
        this.shiftOrigin = shiftOrigin;
        this.sizes = sizes;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return this.shiftOrigin;
    }

    public Pair<Float, Float> getSizes() {
        return this.sizes;
    }
}
