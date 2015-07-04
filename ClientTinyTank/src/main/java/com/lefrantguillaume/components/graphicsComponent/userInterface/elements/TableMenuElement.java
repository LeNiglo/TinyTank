package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.ColorTools;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.ListElement;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import org.newdawn.slick.Input;

import java.util.Map;

/**
 * Created by andres_k on 02/07/2015.
 */
public class TableMenuElement extends TableElement {
    private GenericSendTask genericSendTask;
    private Element focusedElement;

    public TableMenuElement(EnumOverlayElement type, GenericSendTask genericSendTask, BodyRect body) {
        super(type, body, false, new boolean[]{true, true});
        this.genericSendTask = genericSendTask;
        this.focusedElement = null;
    }

    // FUNCTION
    @Override
    public void doTask(Object task) {
        if (task instanceof Element) {
            this.addElement((Element) task);
        } else if (task instanceof Pair) {
            Pair<Integer, Boolean> received = (Pair<Integer, Boolean>) task;
            if (received.getV1() < this.reachable.length) {
                this.reachable[received.getV1()] = received.getV2();
            }
        } else if (task instanceof Integer) {
            int i = 0;
            for (Map.Entry<String, Pair<BodyRect, BodyRect>> entry : this.positionBody.entrySet()) {
                if ((Integer) task == i) {
                    entry.getValue().getV1().setColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_YELLOW));
                } else {
                    entry.getValue().getV1().setColor(null);
                }
                ++i;
            }
        } else if (task instanceof String){
            String value = this.focusedElement.toString();
            String keyString = (String) task;
            String newValue;
            if (value.contains(":")) {
                value = value.substring(0, value.indexOf(":"));
                newValue = StringTools.duplicateString(" ", 14 - value.length()) + keyString + StringTools.duplicateString(" ", 18 - keyString.length());
            } else {
                newValue = keyString;
            }
            this.focusedElement.doTask(newValue);
            this.initFocusElement();
        }
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if (this.isActivated()) {
                this.stop();
                this.initFocusElement();
                return true;
            } else {
                if (this.type == EnumOverlayElement.CUSTOM_MENU) {
                    this.start();
                    return true;
                }
            }
        } else {
            if (this.focusedElement != null) {
                String keyString = Input.getKeyName(key);
                String value = this.focusedElement.toString();
                if (value.contains(":")) {
                    value = value.substring(0, value.indexOf(":"));
                }
                return new Pair<>(EnumInput.getEnumByValue(value), keyString);
            }
        }
        return null;
    }

    @Override
    public boolean isOnFocus(int x, int y) {
        if (this.isActivated()) {
            int listIndex = 0;

            for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
                Object result = entry.getValue().isOnFocus(x, y);
                if (result != null) {
                    if (result instanceof Element) {
                        Element element = (Element) result;
                        if (((Element) result).getType() == EnumOverlayElement.CONTROLS) {
                            this.initFocusElement();
                            this.focusedElement = element;
                            this.focusedElement.setBodyColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_YELLOW));
                        } else if (((Element) result).getType() == EnumOverlayElement.SCREEN) {
                            boolean newValue;
                            if (ColorTools.compareColor(element.getBody().getColor(), ColorTools.Colors.TRANSPARENT_GREEN)) {
                                element.setBodyColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_RED));
                                newValue = false;
                            } else {
                                element.setBodyColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_GREEN));
                                newValue = true;
                            }
                            EnumOverlayElement type = element.getType();
                            if (element.getId().contains(":")){
                                type = EnumOverlayElement.getEnumByValue(element.getId().substring(element.getId().indexOf(":") + 1));
                            }
                            this.genericSendTask.sendTask(new Pair<>(type, new Pair<>(listIndex, newValue)));
                            return true;
                        }
                        return true;
                    } else if (result instanceof Boolean && (Boolean) result == true) {
                        return true;
                    }
                }
                ++listIndex;
            }
        }
        return false;
    }

    private void initFocusElement() {
        if (this.focusedElement != null) {
            this.focusedElement.setBodyColor(null);
            this.focusedElement = null;
        }
    }
}
