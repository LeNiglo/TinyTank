package com.lefrantguillaume.Utils.stockage;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Pair<T1, T2> {
    T1 v1;
    T2 v2;

    public Pair(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T1 getV1() {
        return this.v1;
    }

    public T2 getV2() {
        return this.v2;
    }

    public void setV1(T1 value) {
        this.v1 = value;
    }

    public void setV2(T2 value) {
        this.v2 = value;
    }
}