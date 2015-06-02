package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;

/**
 * Created by andres_k on 24/03/2015.
 */
public class Canon {
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> shiftCanonHead;

    public Canon(Pair<Float, Float> shiftOrigin, Pair<Float, Float> shiftCanonHead){
        this.shiftOrigin = shiftOrigin;
        this.shiftCanonHead = shiftCanonHead;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return this.shiftOrigin;
    }

    public Pair<Float, Float> getShiftCanonHead() {
        return this.shiftCanonHead;
    }
}
