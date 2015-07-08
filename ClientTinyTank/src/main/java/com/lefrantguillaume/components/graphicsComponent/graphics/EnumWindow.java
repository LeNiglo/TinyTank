package com.lefrantguillaume.components.graphicsComponent.graphics;

/**
 * Created by andres_k on 17/03/2015.
 */
public enum EnumWindow {
    EXIT(-1),
    LOGIN(0), ACCOUNT(1), INTERFACE(2), GAME(3);

    private int value;
     EnumWindow(int value) {
         this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
