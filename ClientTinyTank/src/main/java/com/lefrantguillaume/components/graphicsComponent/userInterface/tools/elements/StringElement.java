package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringElement extends Element {
    private StringTimer stringTimer;
    private Color color;

    public StringElement(StringTimer stringTimer, Color color){
        this.body = null;
        this.stringTimer = stringTimer;
        this.color = color;
    }

    public StringElement(BodyRect body, StringTimer stringTimer, Color color){
        this.body = body;
        this.stringTimer = stringTimer;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        if (body != null) {
            this.body.draw(g);
        }
        g.setColor(this.color);
        g.drawString(this.stringTimer.getValue(), this.body.getX(), this.body.getY());
    }

    @Override
    public void draw(Graphics g, BodyRect bodyRect) {
        bodyRect.draw(g);
        g.setColor(this.color);
        g.drawString(this.stringTimer.getValue(), bodyRect.getX(), bodyRect.getY());
    }

    @Override
    public void update() {
    }

    // GETTERS
    @Override
    public boolean isActivated(){
        return this.stringTimer.isActivated();
    }

    @Override
    public boolean isEmpty(){
        return this.stringTimer.getValue().equals("");
    }
}
