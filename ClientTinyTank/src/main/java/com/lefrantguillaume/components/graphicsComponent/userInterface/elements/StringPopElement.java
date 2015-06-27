package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.StringListBody;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 20/06/2015.
 */
public class StringPopElement extends InterfaceElement {
    private StringListBody stringListBody;

    public StringPopElement(Rectangle body) {
        this.parentInit(body);
        this.childInit();
    }

    @Override
    public void parentInit(Rectangle body){
        this.focused = false;
        this.needActivated = false;
        this.body = body;
    }

    public void childInit(){
        this.stringListBody = new StringListBody(this.body, 4);
    }

    // FUNCTIONS

    @Override
    public void draw(Graphics g) {
        this.stringListBody.draw(g);
    }

    @Override
    public void update() {
        this.stringListBody.update();
    }

    @Override
    public Object event(int key, char c) {
        if (key == Input.KEY_K){
            this.stringListBody.addToPrint(new Pair<>(Color.red, "machin a tué bidule"), 3000);
        }
        return null;
    }

    @Override
    public void doTask(Object task) {

    }
}
