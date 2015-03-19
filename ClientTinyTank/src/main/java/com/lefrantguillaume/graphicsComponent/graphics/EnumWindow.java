package com.lefrantguillaume.graphicsComponent.graphics;

/**
 * Created by andres_k on 17/03/2015.
 */
public enum EnumWindow {
    HOME(0), GAME(1);

    private int value;
     EnumWindow(int value) {
         this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
