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
    public Object eventReleased(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if (this.isActivated()) {
                this.stop();
                return true;
            } else {
                if (this.type == EnumOverlayElement.CUSTOM_MENU) {
                    this.start();
                    return true;
                }
            }
        } else {
            if (this.focusedElement != null){
                String keyString = Input.getKeyName(key);
                String value = this.focusedElement.toString();
                String newValue;
                if (value.contains(":")) {
                    value = value.substring(0, value.indexOf(":"));
                    newValue = StringTools.duplicateString(" ", 14 - value.length()) + keyString + StringTools.duplicateString(" ", 18 - keyString.length());
                } else {
                    newValue = keyString;
                }
                this.focusedElement.doTask(newValue);
                this.focusedElement.setBodyColor(null);
                this.focusedElement = null;
                return new Pair<>(EnumInput.getEnumByValue(value), keyString);
            }
        }
        return null;
    }

    @Override
    public boolean isOnFocus(int x, int y) {
        if (this.isActivated()) {
            for (Map.Entry<Element, ListElement> entry : this.table.entrySet()) {
                Object result = entry.getValue().isOnFocus(x, y);

                if (result != null) {
                    if (result instanceof Element) {
                        if (((Element)result).getType() == EnumOverlayElement.CONTROLS) {
                            this.focusedElement = (Element) result;
                            this.focusedElement.setBodyColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_YELLOW));
                        } else if (((Element)result).getType() == EnumOverlayElement.SCREEN){
                            if (ColorTools.compareColor(this.focusedElement.getBody().getColor(), ColorTools.Colors.TRANSPARENT_GREEN)){
                                this.focusedElement.setBodyColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_RED));
                            } else {
                                this.focusedElement.setBodyColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_GREEN));
                            }
                            return true;
                        }
                        return true;
                    } else if (result instanceof Boolean && (Boolean) result == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}