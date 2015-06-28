package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 27/06/2015.
 */
public class BodyRect {
    private Rectangle body;
    private Color color;


    public BodyRect(Rectangle body){
        this.body = body;
        this.color = null;
    }

    public BodyRect(Rectangle body, Color color){
        this.body = body;
        this.color = color;
    }

    // FUNCTIONS
    public void draw(Graphics g){
        if (this.color != null){
            g.setColor(this.color);
            g.fill(this.body);
        }
    }

    public boolean contains(float x, float y){
        return this.body.contains(x, y);
    }

    public boolean isOnFocus(float x, float y){
        return this.body.contains(x, y);
    }

    // GETTERS
    public float getX(){
        return this.body.getMinX();
    }

    public float getY(){
        return this.body.getMinY();
    }

    public float getSizeX(){
        return this.body.getWidth();
    }

    public float getSizeY(){
        return this.body.getHeight();
    }
}
