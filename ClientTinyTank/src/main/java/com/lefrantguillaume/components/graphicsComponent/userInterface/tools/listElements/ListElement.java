package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * Created by andres_k on 27/06/2015.
 */
public abstract class ListElement {
    protected List<Element> elements;
    protected BodyRect body;

    public abstract void draw(Graphics g);

    public abstract void update();

    protected abstract void updatePosition();

    public abstract void addAllToPrint(List<Object> messageData, Element.PositionInBody positionInBody);

    public abstract void addToPrint(Object object, Element.PositionInBody positionInBody);

    public abstract void addToPrint(Object object, long time, Element.PositionInBody positionInBody);

    public Object isOnFocus(float x, float y){
        for (Element element : this.elements){
            if (element.isOnFocus(x, y) && element.isEmpty() == false){
                return element;
            }
        }
        if (body.isOnFocus(x, y)){
            return null;
        }
        return null;
    }

    // SETTERS

    public void setBody(BodyRect body){
        this.body = body;
        this.updatePosition();
    }
}
