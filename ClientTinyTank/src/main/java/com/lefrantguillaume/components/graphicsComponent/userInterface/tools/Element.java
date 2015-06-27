package com.lefrantguillaume.components.graphicsComponent.userInterface.tools;

import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public abstract class Element {
    protected BodyRect body;

    public abstract void draw(Graphics g);

    public abstract void update();
}
