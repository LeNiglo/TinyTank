package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.StringListElement;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andres_k on 27/06/2015.
 */
public class TableElement extends InterfaceElement {
    private List<Element> elements;
    private HashMap<String, StringListElement> table;

    public TableElement(BodyRect body, List<Element> elements){
        this.parentInit(body);
        this.childInit();
        this.elements = elements;
    }

    // INIT
    @Override
    protected void parentInit(BodyRect body) {
        this.body = body;
        this.needActivated = true;
        this.focused = false;
    }

    private void childInit(){
        this.table = new HashMap<>();
    }

    // FUNCTIONS
    @Override
    public void doTask(Object task) {

    }

    @Override
    public void draw(Graphics g) {
        this.body.draw(g);
        for (Element element : this.elements){
            element.draw(g);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public Object event(int key, char c) {
        return null;
    }
}
