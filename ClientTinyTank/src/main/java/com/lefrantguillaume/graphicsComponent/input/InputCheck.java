package com.lefrantguillaume.graphicsComponent.input;

import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputCheck extends Observable {
    public int keyCheck(int key, int mode){
        if (org.newdawn.slick.Input.KEY_ESCAPE == key) {
            return -1;
        }
        else if (key == Input.KEY_DOWN || key == Input.KEY_UP || key == Input.KEY_LEFT || key == Input.KEY_RIGHT
                || key == Input.KEY_A)
        {
            this.setChanged();
            this.notifyObservers(key * mode);
        }
        return 0;
    }
}
