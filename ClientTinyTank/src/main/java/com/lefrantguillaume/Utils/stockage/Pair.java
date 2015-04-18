package com.lefrantguillaume.Utils.stockage;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Pair <T1, T2>{
    T1 key;
    T2 value;

    public Pair(T1 key, T2 value){
        this.key = key;
        this.value = value;
    }

    public T1 getKey(){
        return this.key;
    }

    public T2 getValue(){
        return this.value;
    }
}