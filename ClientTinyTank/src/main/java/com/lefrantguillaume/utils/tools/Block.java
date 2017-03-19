package com.lefrantguillaume.utils.tools;

import com.lefrantguillaume.utils.stockage.Pair;

/**
 * Created by andres_k on 25/03/2015.
 */
public class Block {
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> sizes;

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
