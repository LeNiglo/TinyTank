package com.lefrantguillaume.gui;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by leniglo on 23/05/15.
 */
public class UIList extends UIComponent {
    List<UIButton> uiButtons;
    UUID id;

    public UIList(){
        this.uiButtons = new ArrayList<>();
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean focused(int x, int y) {

        for (int i = 0; i < this.uiButtons.size(); ++i){
            if (this.uiButtons.get(i).focused(x, y))
                return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics graphics) {
        //draw le fond

        for (int i = 0; i < this.uiButtons.size(); ++i){
            this.uiButtons.get(i).draw(graphics);
        }
    }

    @Override
    public void addInput(int key) {
    //do nothing
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
