package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
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

    public abstract Boolean isOnFocus(int x, int y);

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
