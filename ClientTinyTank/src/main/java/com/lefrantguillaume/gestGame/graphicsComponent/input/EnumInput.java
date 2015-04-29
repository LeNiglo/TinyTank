package com.lefrantguillaume.gestGame.graphicsComponent.input;

/**
 * Created by andres_k on 16/03/2015.
 */
public enum EnumInput {
    PRESSED(1), RELEASED(-1);

    private final int value;

    EnumInput(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
