package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.stockage.Pair;
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

    public CustomElement(EnumOverlayElement type, GenericSendTask genericSendTask, BodyRect body, boolean activated, boolean[] needActivated) {
        this.parentInit(body, type, activated, needActivated);
        this.childInit(genericSendTask);
    }

    // INIT
    public void childInit(GenericSendTask genericSendTask) {
        this.elements = new ArrayList<>();
        this.genericSendTask = genericSendTask;
    }

    // FUNCTIONS
    @Override
    public void doTask(Object task) {
        if (task instanceof Element) {
            this.elements.add((Element) task);
        } else {
            for (Element element : this.elements) {
                element.doTask(task);
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
         if (key == Input.KEY_ESCAPE) {
            if (this.isActivated()) {
                this.activatedTimer.stopTimer();
                return true;
            } else {
                if (this.type == EnumOverlayElement.CUSTOM_MENU) {
                    this.activatedTimer.startTimer();
                    return true;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isOnFocus(int x, int y) {
        if (this.isActivated()) {
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
}
