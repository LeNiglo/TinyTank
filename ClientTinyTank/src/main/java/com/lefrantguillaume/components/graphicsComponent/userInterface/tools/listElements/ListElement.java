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

    public abstract void addAllToPrint(List<Object> messageData);

    public abstract void addToPrint(Object object);

    public abstract void addToPrint(Object object, long time);

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
}
