package com.lefrantguillaume.gui;

import org.newdawn.slick.Graphics;

import java.util.UUID;

/**
 * Created by leniglo on 23/05/15.
 */
public abstract class UIComponent {

    public abstract boolean focused(int x, int y);
    public abstract void draw(Graphics graphics);
    public abstract void addInput(int key);
    public abstract UUID getId();
}
