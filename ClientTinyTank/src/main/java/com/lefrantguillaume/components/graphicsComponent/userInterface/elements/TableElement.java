package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.ImageListElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.ListElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.StringListElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andres_k on 27/06/2015.
 */
public class TableElement extends InterfaceElement {
    private HashMap<Element, ListElement> table;
    private HashMap<String, Pair<BodyRect, BodyRect>> positionBody;

    public TableElement(BodyRect body) {
        this.parentInit(body);
        this.childInit();
    }

    // INIT
    @Override
    protected void parentInit(BodyRect body) {
        this.body = body;
        this.needActivated = true;
        this.activatedTimer = new ActivatedTimer(true);
        this.type = EnumOverlayElement.TABLE;
    }

    private void childInit() {
        this.table = new HashMap<>();
        this.positionBody = new HashMap<>();
    }

    // FUNCTIONS
    @Override
    public void doTask(Object task) {
        if (task instanceof Element) {
            Element item = (Element) task;
            Element key = this.containsKey(item);
            if (key != null) {
                Debug.debug("add elem");
                item.setBody(this.positionBody.get(key.getId()).getV2());
                this.table.get(key).addToPrint(item, Element.PositionInBody.MIDDLE_MID);
            } else {
                if (item.getType() == EnumOverlayElement.STRING) {
                    this.table.put(item, new StringListElement());
                } else if (item.getType() == EnumOverlayElement.IMAGE) {
                    Debug.debug("add table");
                    this.table.put(item, new ImageListElement());
                }
                this.initPositionBody();
                this.initTableBody();
            }
        }

    }

    @Override
    public void draw(Graphics g) {
        this.body.draw(g);
        for (Map.Entry<Element, ListElement> item : this.table.entrySet()) {
            item.getKey().draw(g, this.positionBody.get(item.getKey().getId()).getV1());
            item.getValue().draw(g);
        }
    }

    @Override
    public void update() {
        for (Map.Entry<Element, ListElement> item : this.table.entrySet()) {
            item.getKey().update();
            item.getValue().update();
        }
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

    private Element containsKey(Element item) {
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
            if (item.getId().equals(entry.getKey().getId())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void initPositionBody() {
        float currentX = this.body.getMinX();
        float width = this.body.getSizeX() / this.table.size();
        float currentY = this.body.getMinY();

        this.positionBody.clear();
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
            float height = this.body.getSizeY() - entry.getKey().getAbsoluteHeight();

            if (entry.getKey().getType() == EnumOverlayElement.IMAGE) {
                this.positionBody.put(entry.getKey().getId(), new Pair<>(new BodyRect(new Rectangle(currentX, currentY, width, height)),
                        new BodyRect(new Rectangle(currentX, currentY + entry.getKey().getAbsoluteHeight(), width, height))));
            } else if (entry.getKey().getType() == EnumOverlayElement.STRING) {
                this.positionBody.put(entry.getKey().getId(), new Pair<>(new BodyRect(new Rectangle(currentX, currentY, width, height), new Color(0.1f, 0.2f, 0.3f, 0.5f)),
                        new BodyRect(new Rectangle(currentX, currentY + entry.getKey().getAbsoluteHeight(), width, height), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
            }
            currentX += width;
        }
    }

    private void initTableBody() {
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
            if (this.positionBody.containsKey(entry.getKey().getId())){
                entry.getValue().setBody(this.positionBody.get(entry.getKey().getId()).getV2());
            }
        }
    }
}
