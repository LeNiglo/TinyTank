package com.lefrantguillaume.utils.stockage;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tuple<T1, T2, T3> {
    T1 v1;
    T2 v2;
    T3 v3;

    public Tuple(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public T1 getV1() {
        return this.v1;
    }

    public T2 getV2() {
        return this.v2;
    }

    public T3 getV3() {
        return this.v3;
    }
}
