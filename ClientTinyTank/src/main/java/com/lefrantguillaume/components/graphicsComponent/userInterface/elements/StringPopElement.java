package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 20/06/2015.
 */
public class StringPopElement extends InterfaceElement {

    public StringPopElement() {
        this.focused = false;
        this.needActivated = false;
        this.body = new Rectangle(WindowConfig.getSizeX() - 200, 20, 400, 200);

        this.initMessageBodies();
    }

    public void initMessageBodies() {
    }

    // FUNCTIONS

    @Override
    public void draw(Graphics g) {
    }

    @Override
    public void update() {
    }

    @Override
    public Object event(int key, char c) {
        return null;
    }

    @Override
    public void doTask(Object task) {
    }
}
