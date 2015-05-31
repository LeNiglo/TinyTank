package com.lefrantguillaume.gui;

import org.newdawn.slick.Graphics;

import java.util.UUID;

/**
 * Created by leniglo on 23/05/15.
 */
public class UIButton extends UIComponent {
    private UUID id;

    public UIButton(){
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean focused(int x, int y) {
        return false;
    }

    @Override
    public void draw(Graphics graphics) {

    }

    @Override
    public void addInput(int key) {

    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
