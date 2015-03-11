package com.lefrantguillaume.graphicsComponent.input;

import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputCheck extends Observable {
    public int keyCheck(int key){
        if (org.newdawn.slick.Input.KEY_ESCAPE == key) {
            return -1;
        }
        else if (Input.KEY_A == key)
        {
            setChanged();
            notifyObservers(key);
        }
        return 0;
    }
}
