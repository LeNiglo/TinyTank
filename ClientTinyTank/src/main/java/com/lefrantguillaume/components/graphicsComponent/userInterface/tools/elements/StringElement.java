package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringElement extends Element {
    private StringTimer value;
    private Color color;

    public StringElement(BodyRect body, StringTimer value, Color color){
        this.body = body;
        this.value = value;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        this.body.draw(g);
        g.setColor(this.color);
        g.drawString(this.value.getValue(), this.body.getX(), this.body.getY());
    }

    @Override
    public void update() {
    }

    // GETTERS
    public boolean isActivated(){
        return this.value.isActivated();
    }
}
