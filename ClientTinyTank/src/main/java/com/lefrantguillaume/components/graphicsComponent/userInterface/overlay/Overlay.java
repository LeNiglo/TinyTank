package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.input.InputData;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Graphics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 05/07/2015.
 */
public abstract class Overlay extends Observable implements Observer {
    protected int current;
    protected OverlayConfigs overlayConfigs;
    protected Map<EnumOverlayElement, InterfaceElement> elements;
    protected AnimatorOverlayData animatorOverlayData;
    protected GenericSendTask genericSendTask;
    protected InputData inputData;

    protected Overlay(InputData inputData) throws JSONException {
        this.current = 0;
        this.inputData = inputData;
        this.overlayConfigs = new OverlayConfigs("configPreferenceOverlay.json");

        this.genericSendTask = new GenericSendTask();
        this.genericSendTask.addObserver(this);

        this.elements = new LinkedHashMap<>();
    }

    // INIT
    public abstract void initElements();
    public abstract void initElementsComponent(AnimatorOverlayData animatorOverlayData);

    public void initPreference() {
        for (Map.Entry<EnumOverlayElement, boolean[]> entry : this.overlayConfigs.getAvailableData().entrySet()) {
            if (this.elements.containsKey(entry.getKey())) {
                this.elements.get(entry.getKey()).setReachable(entry.getValue());
            }
        }
    }

    // FUNCTIONS
    public void leave() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().leave();
        }
    }

    public void draw(Graphics g) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                entry.getValue().draw(g);
            }
        }
    }

    public void updateOverlay() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().update();
        }
    }

    public abstract void doTask(Object task);

    public abstract boolean event(int key, char c, EnumInput type);

    public boolean isOnFocus(int x, int y) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                if (entry.getValue().isOnFocus(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFocused() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                if (entry.getValue().isActivated()) {
                    return true;
                }
            }
        }
        return false;
    }
}
