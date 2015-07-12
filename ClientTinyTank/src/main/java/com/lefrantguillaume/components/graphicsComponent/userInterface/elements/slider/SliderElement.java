package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.slider;

import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.tools.ConsoleWriter;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.ListElement;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andres_k on 06/07/2015.
 */
public class SliderElement extends InterfaceElement {
    private InterfaceElement interfaceElement;
    private HashMap<EnumOverlayElement, ListElement> elements;
    private EnumOverlayElement last;

    public SliderElement(InterfaceElement interfaceElement) {
        this.interfaceElement = interfaceElement;
        this.elements = new HashMap<>();
        this.last = EnumOverlayElement.NOTHING;
    }

    @Override
    public boolean sliderMove(int x, int y) {

        Object result = this.interfaceElement.isOnFocus(x, y);

        if (result != null && result instanceof Element) {
            this.select(((Element) result).getType());
            return true;
        } else if (result != null && result instanceof EnumOverlayElement) {
            this.select((EnumOverlayElement) result);
            return true;
        } else if (this.last != EnumOverlayElement.NOTHING){
            this.unselectAll();
            this.last = EnumOverlayElement.NOTHING;
        }
        return false;
    }

    @Override
    public void start() {
        this.interfaceElement.start();
    }

    @Override
    public void stop() {
        this.interfaceElement.stop();
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof Pair && ((Pair) task).getV1() instanceof EnumOverlayElement && ((Pair) task).getV2() instanceof Element) {

            if (this.elements.containsKey(((Pair) task).getV1())) {
                ConsoleWriter.debug("Add new String: " + ((Element) ((Pair) task).getV2()).getType());
                this.elements.get(((Pair) task).getV1()).addToPrint(((Pair) task).getV2(), Element.PositionInBody.LEFT_MID);
            }
        } else if (task instanceof Pair && ((Pair) task).getV1() instanceof EnumOverlayElement && ((Pair) task).getV2() instanceof ListElement) {
            ConsoleWriter.debug("Add new List: " + ((Pair) task).getV2());
            ((ListElement) ((Pair) task).getV2()).getBody().setPrintable(false);
            this.elements.put((EnumOverlayElement) ((Pair) task).getV1(), (ListElement) ((Pair) task).getV2());
        } else {
            this.interfaceElement.doTask(task);

        }
    }

    @Override
    public void leave() {
        this.interfaceElement.leave();
    }

    @Override
    public void draw(Graphics g) {
        this.interfaceElement.draw(g);
        for (Map.Entry<EnumOverlayElement, ListElement> entry : this.elements.entrySet()) {
            if (entry.getValue().getBody().isPrintable()) {
                entry.getValue().draw(g);
            }
        }
    }

    @Override
    public void update() {
        this.interfaceElement.update();
    }

    @Override
    public void clearData() {
        this.interfaceElement.clearData();
        for (Map.Entry<EnumOverlayElement, ListElement> entry : this.elements.entrySet()){
            entry.getValue().clear();
        }
        this.elements.clear();
        this.last = null;
    }

    @Override
    public Object eventPressed(int key, char c) {
        return this.interfaceElement.eventPressed(key, c);
    }

    @Override
    public Object eventReleased(int key, char c) {
        return this.interfaceElement.eventReleased(key, c);
    }

    @Override
    public Object isOnFocus(int x, int y) {
        return this.interfaceElement.isOnFocus(x, y);
    }

    public boolean select(EnumOverlayElement type) {
        if (this.last != type) {
            if (this.elements.containsKey(type) && this.elements.get(type).getBody().isPrintable() == false) {
                this.unselect(this.last);
                this.elements.get(type).getBody().setPrintable(true);
                this.last = type;
                return true;
            }
        }
        return false;
    }

    public void unselect(EnumOverlayElement type){
        if (this.elements.containsKey(type)){
            this.elements.get(type).getBody().setPrintable(false);
        }
    }

    public void unselectAll() {
        for (Map.Entry<EnumOverlayElement, ListElement> entry : this.elements.entrySet()) {
            entry.getValue().getBody().setPrintable(false);
        }
    }

    // GETTERS
    @Override
    public boolean isActivated() {
        return this.interfaceElement.isActivated();
    }

    @Override
    public boolean[] getReachable() {
        return this.interfaceElement.getReachable();
    }

    @Override
    public EnumOverlayElement getType() {
        return this.interfaceElement.getType();
    }

    @Override
    public BodyRect getBody() {
        return this.interfaceElement.getBody();
    }

    // SETTERS
    @Override
    public void setReachable(boolean[] reachable) {
        this.interfaceElement.setReachable(reachable);
    }
}
