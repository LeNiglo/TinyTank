package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 23/06/2015.
 */
public abstract class InterfaceElement {
    protected BodyRect body;
    protected boolean focused;
    protected boolean needActivated;

    // FUNCTION
    protected abstract void parentInit(BodyRect body);

    public abstract void doTask(Object task);

    public abstract void draw(Graphics g);

    public abstract void update();

    public abstract Object event(int key, char c);

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

    public boolean isNeedActivated(){
        return this.needActivated;
    }

    // SETTERS
    public void setFocused(boolean value){
        this.focused = value;
    }
}
