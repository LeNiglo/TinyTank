package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.custom;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 03/07/2015.
 */
public class CustomElement extends InterfaceElement {
    private List<Element> elements;
    private GenericSendTask genericSendTask;
    private boolean canBeActivate;
    private Pair<Float, Float> saves;

    public CustomElement(EnumOverlayElement type, GenericSendTask genericSendTask, BodyRect body, boolean activated, boolean[] needActivated) {
        this.parentInit(body, type, activated, needActivated);
        this.childInit(genericSendTask);
    }

    public CustomElement(EnumOverlayElement type, BodyRect body, boolean activated, boolean[] needActivated) {
        this.parentInit(body, type, activated, needActivated);
        this.childInit(null);
    }

    // INIT
    public void childInit(GenericSendTask genericSendTask) {
        this.elements = new ArrayList<>();
        this.genericSendTask = genericSendTask;
        if (!this.isActivated()) {
            this.canBeActivate = true;
        } else {
            this.canBeActivate = false;
        }
        if (this.body != null){
            this.saves = new Pair<>(this.body.getMaxX(), this.body.getMaxY());
        }
    }

    // FUNCTIONS
    @Override
    public void doTask(Object task) {
        if (task instanceof Element) {
            this.elements.add((Element) task);
        } else if (task instanceof Pair) {
            if (((Pair) task).getV1() instanceof Integer) {
                Pair<Integer, Boolean> received = (Pair<Integer, Boolean>) task;
                if (received.getV1() < this.reachable.length) {
                    this.reachable[received.getV1()] = received.getV2();
                }
            } else if (((Pair) task).getV1() instanceof EnumOverlayElement) {
                Pair<EnumOverlayElement, Object> received = (Pair<EnumOverlayElement, Object>) task;

                if (!received.getV1().getValue().equals("")) {
                    for (Element element : this.elements) {
                        if (element.getId().contains(received.getV1().getValue())) {
                            element.doTask(received.getV2());
                        }
                    }
                    if (this.type == EnumOverlayElement.CUSTOM_USER_STAT) {
                        this.body.setSizes(this.body.getSizeX(), 10 + this.sizeYOfBorders() + (this.elementsPrintable() - 1));
                        this.body.setPosition(this.saves.getV1() - this.body.getSizeX(), this.saves.getV2() - this.body.getSizeY());
                    }
                }
            }
        }
    }

    @Override
    public void leave() {
        this.activatedTimer.leave();
        for (Element element : this.elements) {
            element.leave();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (this.isActivated()) {
            this.body.draw(g);
            for (Element element : this.elements) {
                element.draw(g);
            }
        }
    }

    @Override
    public void update() {
        for (Element element : this.elements) {
            element.update();
        }
    }

    @Override
    public Object eventPressed(int key, char c) {
        return null;
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (key == Input.KEY_ESCAPE && this.canBeActivate == true) {
            if (this.isActivated()) {
                this.activatedTimer.stopTimer();
                return true;
            } else {
                this.activatedTimer.startTimer();
                return true;
            }
        }
        return null;
    }

    @Override
    public boolean isOnFocus(int x, int y) {
        if (this.isActivated() && this.genericSendTask != null) {
            for (Element element : this.elements) {
                Object result = element.isOnFocus(x, y);
                if (result != null && element.isEmpty() == false) {
                    if (result instanceof EnumOverlayElement) {
                        this.genericSendTask.sendTask(new Pair<>(this.type, result));
                        return true;
                    }
                    if (result instanceof Boolean && (Boolean) result == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int containId(String id) {
        for (int i = 0; i < this.elements.size(); ++i) {
            if (!id.equals("") && this.elements.get(i).getId().contains(id)) {
                return i;
            }
        }
        return -1;
    }

    public int elementsPrintable(){
        int result = 0;

        for (Element element : this.elements){
            if (element.isBodyPrintable() && !element.getId().contains(EnumOverlayElement.BORDER.getValue())){
                result += 1;
            }
        }
        return result;
    }

    public int sizeYOfBorders() {
        int result = 0;
        for (Element element : this.elements) {
            if (element.isBodyPrintable() && element.getId().contains(EnumOverlayElement.BORDER.getValue())) {
                result += element.getBody().getSizeY();
            }
        }
        return result;
    }
}
