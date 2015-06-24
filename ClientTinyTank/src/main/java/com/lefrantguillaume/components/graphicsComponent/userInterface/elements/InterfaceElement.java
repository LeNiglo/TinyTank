package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 23/06/2015.
 */
public class InterfaceElement {
    protected Rectangle body;
    protected boolean focused;

    // FUNCTION
    public void doTask(Object task){
    }

    public void draw(Graphics g){
    }

    public void update(){
    }

    public Object event(int key, char c){
        return null;
    }

    public boolean isOnFocus(int x, int y){
        if (this.body.contains(x, y)){
            this.focused = true;
            return true;
        }
        this.focused = false;
        return false;
    }

    // GETTERS
    public boolean isFocused() {
        return this.focused;
    }

    public Rectangle getBody(){
        return this.body;
    }

    // SETTERS
    public void setFocused(boolean value){
        this.focused = value;
    }
}
