package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public abstract class Element {
    protected BodyRect body;

    public abstract void draw(Graphics g);

    public abstract void draw(Graphics g, BodyRect body);

    public abstract void update();

    public boolean isOnFocus(float x, float y){
        if (this.body == null){
            return false;
        }
        return this.body.isOnFocus(x, y);
    }

    public abstract boolean isActivated();

    public abstract boolean isEmpty();

    public void setBody(BodyRect body){
        this.body = body;
    }
}
