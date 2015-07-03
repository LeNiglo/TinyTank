package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 03/07/2015.
 */
public class ButtonElement extends Element {
    private Element element;

    public ButtonElement(Element element, EnumOverlayElement type) {
        this.element = element;
        this.type = type;
    }

    @Override
    public void leave() {
        this.element.leave();
    }

    @Override
    public void draw(Graphics g) {
        this.element.draw(g);
    }

    @Override
    public void draw(Graphics g, BodyRect body) {
        this.element.draw(g, body);
    }

    @Override
    public void update() {
        this.element.update();
    }

    @Override
    public boolean replace(Element element) {
        this.element.leave();
        this.element = element;
        return true;
    }

    @Override
    public Object doTask(Object task) {
        return this.element.doTask(task);
    }

    @Override
    public boolean isActivated() {
        return this.element.isActivated();
    }

    @Override
    public boolean isEmpty() {
        return this.element.isEmpty();
    }

    @Override
    public float getAbsoluteWidth() {
        return this.element.getAbsoluteWidth();
    }

    @Override
    public float getAbsoluteHeight() {
        return this.element.getAbsoluteHeight();
    }

    @Override
    public String toString() {
        return this.element.toString();
    }

    @Override
    public Object isOnFocus(float x, float y){
        if (this.element.isOnFocus(x, y) != null){
            return this.type;
        } else {
            return null;
        }
    }
}
