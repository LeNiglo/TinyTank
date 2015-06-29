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

    public StringElement(StringTimer stringTimer, Color color) {
        this.body = null;
        this.stringTimer = stringTimer;
        this.color = color;
        this.id = "";
    }

    public StringElement(StringTimer stringTimer, Color color, String id) {
        this.body = null;
        this.stringTimer = stringTimer;
        this.color = color;
        this.id = id;
    }

    public StringElement(BodyRect body, StringTimer stringTimer, Color color) {
        this.body = body;
        this.stringTimer = stringTimer;
        this.color = color;
        this.id = "";
    }

    public StringElement(BodyRect body, StringTimer stringTimer, Color color, String id) {
        this.body = body;
        this.stringTimer = stringTimer;
        this.color = color;
        this.id = id;
    }

    @Override
    public void draw(Graphics g) {
        int begin;

        if (body != null) {
            this.body.draw(g);
            begin = this.stringTimer.getValue().length() - (int) (this.body.getSizeX() / 10);
            if (begin < 0) {
                begin = 0;
            }
            g.setColor(this.color);
            g.drawString(this.stringTimer.getValue().substring(begin), this.body.getX(), this.body.getY());
        }
    }

    @Override
    public void draw(Graphics g, BodyRect bodyRect) {
        int begin;

        bodyRect.draw(g);
            bodyRect.draw(g);
            begin = this.stringTimer.getValue().length() - (int) (bodyRect.getSizeX() / 10);
            if (begin < 0) {
                begin = 0;
            }
        g.setColor(this.color);
        g.drawString(this.stringTimer.getValue().substring(begin), bodyRect.getX(), bodyRect.getY());
    }

    @Override
    public void update() {
    }

    public void addToValue(int position, String add){
        this.stringTimer.add(position, add);
    }

    public void deleteValue(int start, int number){
        this.stringTimer.delete(start, number);
    }

    // GETTERS
    @Override
    public boolean isActivated() {
        return this.stringTimer.isActivated();
    }

    @Override
    public boolean isEmpty() {
        return this.stringTimer.getValue().equals("");
    }

    public String getValue(){
        return this.stringTimer.getValue();
    }

    // SETTERS
    public void setValue(String value) {
        this.stringTimer.setValue(value);
    }

    @Override
    public String toString(){
        return this.stringTimer.getValue();
    }
}
