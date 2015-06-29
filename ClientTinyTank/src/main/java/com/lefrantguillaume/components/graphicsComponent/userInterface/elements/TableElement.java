package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.ListElement;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 27/06/2015.
 */
public class TableElement extends InterfaceElement {
    private List<Element> elements;
    private List<ListElement> table;

    public TableElement(BodyRect body){
        this.parentInit(body);
        this.childInit();
    }

    // INIT
    @Override
    protected void parentInit(BodyRect body) {
        this.body = body;
        this.needActivated = true;
        this.activatedTimer = new ActivatedTimer(false);
    }

    private void childInit(){
        this.table = new ArrayList<>();
        this.elements = new ArrayList<>();
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
    public Object eventPressed(int key, char c) {
        return null;
    }

    @Override
    public Object eventReleased(int key, char c) {
        return null;
    }

    @Override
    public Boolean isOnFocus(int x, int y) {
        return false;
    }

}
