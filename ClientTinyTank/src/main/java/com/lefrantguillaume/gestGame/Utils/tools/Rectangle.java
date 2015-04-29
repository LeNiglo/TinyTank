package com.lefrantguillaume.gestGame.Utils.tools;

import com.lefrantguillaume.gestGame.Utils.stockage.Pair;

/**
 * Created by andres_k on 25/03/2015.
 */
public class Rectangle {
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> sizes;

    public Rectangle(Pair<Float, Float> shiftOrigin, Pair<Float, Float> sizes) {
        this.shiftOrigin = shiftOrigin;
        this.sizes = sizes;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return shiftOrigin;
    }

    public Pair<Float, Float> getSizes() {
        return sizes;
    }
}
