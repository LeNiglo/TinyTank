package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public abstract class Element {
    protected BodyRect body;
    protected String id;
    protected EnumOverlayElement type;
    protected PositionInBody position;

    public static enum PositionInBody {
        LEFT_UP, MIDDLE_UP, RIGHT_UP,
        LEFT_MID, MIDDLE_MID, RIGHT_MID,
        LEFT_DOWN, MIDDLE_DOWN, RIGHT_DOWN
    }

    protected void init(BodyRect body, String id, PositionInBody position, EnumOverlayElement type){
        this.body = body;
        this.id = id;
        this.position = position;
        this.type = type;
    }

    public abstract void leave();

    public abstract void draw(Graphics g);

    public abstract void draw(Graphics g, BodyRect body);

    public abstract void update();

    public abstract boolean replace(Element element);

    public abstract Object doTask(Object task);

    // GETTERS
    public Object isOnFocus(float x, float y){
        if (this.body != null && this.body.isOnFocus(x, y)){
            return true;
        }
        return null;
    }

    public abstract boolean isActivated();

    public abstract boolean isEmpty();

    public String getId(){
        return this.id;
    }

    public EnumOverlayElement getType(){
        return this.type;
    }

    public BodyRect getBody(){
        return this.body;
    }

    public abstract float getAbsoluteWidth();

    public abstract float getAbsoluteHeight();

    // SETTERS
    public void setBody(BodyRect body){
        if (this.body != null) {
            if (body.getColor() == null){
                body.setColor(this.body.getColor());
            }
        }
        this.body = body;
    }

    public void setBodyColor(Color color){
        this.body.setColor(color);
    }

    public abstract String toString();
}
