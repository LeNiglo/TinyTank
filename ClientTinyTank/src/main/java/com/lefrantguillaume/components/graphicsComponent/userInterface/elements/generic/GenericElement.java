package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.generic;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.sounds.MusicController;
import com.lefrantguillaume.components.graphicsComponent.sounds.SoundController;
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
public class GenericElement extends InterfaceElement {
    private List<Element> elements;
    private GenericSendTask genericSendTask;
    private Pair<Boolean, Boolean> canBeActivate;
    private Pair<Float, Float> saves;

    public GenericElement(EnumOverlayElement type, GenericSendTask genericSendTask, BodyRect body, Pair<Boolean, Boolean> canBeActivate, boolean activated, boolean[] needActivated) {
        this.parentInit(body, type, activated, needActivated);
        this.childInit(genericSendTask, canBeActivate);
    }

    public GenericElement(EnumOverlayElement type, BodyRect body, Pair<Boolean, Boolean> canBeActivate, boolean activated, boolean[] needActivated) {
        this.parentInit(body, type, activated, needActivated);
        this.childInit(null, canBeActivate);
    }

    // INIT
    public void childInit(GenericSendTask genericSendTask, Pair<Boolean, Boolean> canBeActivate) {
        this.elements = new ArrayList<>();
        this.genericSendTask = genericSendTask;
        this.canBeActivate = canBeActivate;
        if (this.body != null) {
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
                    if (this.type == EnumOverlayElement.GENERIC_USER_STAT) {
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
    public void clearData() {
        this.elements.clear();
    }

    @Override
    public Object eventPressed(int key, char c) {
        return null;
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if (!this.isActivated() && this.canBeActivate.getV1() == true) {
                this.activatedTimer.startTimer();
                return true;
            } else if (this.canBeActivate.getV2() == true) {
                this.activatedTimer.stopTimer();
            }
        }
        return null;
    }

    @Override
    public Object isOnFocus(int x, int y) {
        boolean onFocus = false;

        if (this.type == EnumOverlayElement.TABLE_MENU_SETTINGS)
            Debug.debug("GENERIC IS ON FOCUS");
        if (this.isActivated()) {
            for (Element element : this.elements) {
                Object result = element.isOnFocus(x, y);
                if (this.type == EnumOverlayElement.TABLE_MENU_SETTINGS)
                    Debug.debug("for element: '" + element.getId() + "' -> " + result);
                if (result != null) {
                    if (result instanceof EnumOverlayElement) {
                        if (this.genericSendTask != null) {
                            this.genericSendTask.sendTask(new Pair<>(this.type, result));
                        }
                        return result;
                    }
                    if (result instanceof Boolean && (Boolean) result == true) {

                        if (element.getId().equals(EnumOverlayElement.MUSICS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue())) {
                            int graph = this.containId(EnumOverlayElement.MUSICS_GRAPH.getValue());
                            int value = this.containId(EnumOverlayElement.MUSICS_VALUE.getValue());

                            if (graph != -1 && value != -1) {
                                Element itemGraph = this.elements.get(graph);
                                Element itemValue = this.elements.get(value);
                                float border = itemGraph.getBody().getMinX() - element.getBody().getMinX();
                                float percent = (x - itemGraph.getBody().getMinX()) / (element.getBody().getSizeX() - (border * 2));

                                MusicController.changeVolume(percent * MusicController.getMaxVolume());

                                itemValue.doTask(String.valueOf((int)(MusicController.getVolume() * 100)));
                                itemGraph.doTask(new Pair<>("cutBody", MusicController.getVolume() / MusicController.getMaxVolume()));
                            }
                        } else if (element.getId().equals(EnumOverlayElement.SOUNDS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue())) {
                            int graph = this.containId(EnumOverlayElement.SOUNDS_GRAPH.getValue());
                            int value = this.containId(EnumOverlayElement.SOUNDS_VALUE.getValue());

                            if (graph != -1 && value != -1) {
                                Element itemGraph = this.elements.get(graph);
                                Element itemValue = this.elements.get(value);
                                float border = itemGraph.getBody().getMinX() - element.getBody().getMinX();
                                float percent = (x - itemGraph.getBody().getMinX()) / (element.getBody().getSizeX() - (border * 2));

                                SoundController.changeVolume(percent * SoundController.getMaxVolume());

                                itemValue.doTask(String.valueOf((int)(SoundController.getVolume() * 100)));
                                itemGraph.doTask(new Pair<>("cutBody", SoundController.getVolume() / SoundController.getMaxVolume()));
                            }
                        }
                        onFocus = true;
                    }
                }
            }
        }
        if (onFocus == true){
            return onFocus;
        }
        return null;
    }

    public int containId(String id) {
        for (int i = 0; i < this.elements.size(); ++i) {
            if (!id.equals("") && this.elements.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public int elementsPrintable() {
        int result = 0;

        for (Element element : this.elements) {
            if (element.isBodyPrintable() && !element.getId().contains(EnumOverlayElement.BORDER.getValue())) {
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
