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

    public TableElement(EnumOverlayElement type, BodyRect body, boolean activated, boolean needActivatedParent) {
        this.parentInit(body, type, activated, needActivatedParent);
        this.childInit();
    }

    // INIT
    @Override
    protected void parentInit(BodyRect body, EnumOverlayElement type, boolean activated, boolean needActivatedParent) {
        this.body = body;
        this.activatedTimer = new ActivatedTimer(activated);
        this.type = type;
        this.needActivated = needActivatedParent;
    }

    private void childInit() {
        this.table = new HashMap<>();
        this.positionBody = new HashMap<>();
    }

    // FUNCTIONS
    @Override
    public void doTask(Object task) {
        if (task instanceof Element) {
            this.addElement((Element) task);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (this.isActivated()) {
            this.body.draw(g);
            for (Map.Entry<Element, ListElement> item : this.table.entrySet()) {
                item.getKey().draw(g, this.positionBody.get(item.getKey().getId()).getV1());
                item.getValue().draw(g);
            }
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

    public void addElement(Element item) {
        Element key = this.containsKey(item);
        if (key != null) {
            Debug.debug("add elem: " + item.toString());
            if (checkSameHeadId(item.getId())){
                key.replace(item);
            } else {
                this.table.get(key).setBody(this.positionBody.get(key.getId()).getV2());
                this.table.get(key).addToPrint(item, Element.PositionInBody.MIDDLE_MID);
            }
        } else {
            if (this.checkSameHeadId(item.getId())) {
                if (item.getType() == EnumOverlayElement.STRING) {
                    this.table.put(item, new StringListElement());
                } else if (item.getType() == EnumOverlayElement.IMAGE) {
                    this.table.put(item, new ImageListElement());
                } else {
                    return;
                }
                Debug.debug("add table: " + item.getId());
                this.initPositionBody();
                this.initTableBody();
            }
        }
    }

    private boolean checkSameHeadId(String id) {
        if (id.contains(":")) {
            String v1 = id.substring(0, id.indexOf(":"));
            String v2 = id.substring(id.indexOf(":") + 1, id.length());
            return v1.equals(v2);
        }
        return true;
    }

    private boolean containsHeadId(String head, String id){
        if (head.contains(":")){
            return head.substring(0, head.indexOf(":")).equals(id.substring(0, id.indexOf(":")));
        } else {
            return head.equals(id);
        }
    }

    private Element containsKey(Element item) {
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
            if (this.containsHeadId(entry.getKey().getId(), item.getId())) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected void sendTaskToAll(Object task){
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()){
            entry.getKey().doTask(task);
            entry.getValue().sendTask(task);
        }
    }

    private void initPositionBody() {
        float border = 10;
        float currentX = this.body.getMinX();
        float width = this.body.getSizeX() / this.table.size();
        float currentY = this.body.getMinY();

        this.positionBody.clear();
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
            float height = this.body.getSizeY() - entry.getKey().getAbsoluteHeight() - border;

            if (entry.getKey().getType() == EnumOverlayElement.IMAGE) {
                this.positionBody.put(entry.getKey().getId(), new Pair<>(new BodyRect(new Rectangle(currentX + border, currentY, width, height)),
                        new BodyRect(new Rectangle(currentX + border, currentY + entry.getKey().getAbsoluteHeight(), width - (border * 2), height))));
            } else if (entry.getKey().getType() == EnumOverlayElement.STRING) {
                this.positionBody.put(entry.getKey().getId(), new Pair<>(new BodyRect(new Rectangle(currentX + border, currentY, width, height)),
                        new BodyRect(new Rectangle(currentX + border, currentY + entry.getKey().getAbsoluteHeight(), width - (border * 2), height), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
            }
            currentX += width;
        }
    }

    private void initTableBody() {
        for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
            if (this.positionBody.containsKey(entry.getKey().getId())) {
                entry.getValue().setBody(this.positionBody.get(entry.getKey().getId()).getV2());
            }
        }
    }
}
