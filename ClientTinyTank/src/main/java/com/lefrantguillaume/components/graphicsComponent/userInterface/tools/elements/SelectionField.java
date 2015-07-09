package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Created by andres_k on 29/06/2015.
 */
public class SelectionField extends Element {
    private StringElement stringElement;
    private boolean focused;
    private String target;

    public SelectionField(StringElement stringElement, String id) {
        this.stringElement = stringElement;
        this.focused = false;
        this.type = EnumOverlayElement.SELECT_SHIELD;
        this.id = id;
        this.target = "";
    }

    @Override
    public void leave() {
        this.stringElement.leave();
    }

    public void draw(Graphics g) {
        if (this.focused) {
            this.stringElement.draw(g);
        }
    }

    @Override
    public void draw(Graphics g, BodyRect body) {
        if (focused) {
            this.stringElement.draw(g, body);
        }
    }

    @Override
    public void update() {
        this.stringElement.update();
    }

    @Override
    public boolean replace(Element element) {
        return this.stringElement.replace(element);
    }

    @Override
    public Object doTask(Object task) {
        if (task instanceof Pair && ((Pair) task).getV1() instanceof String) {
            String order = (String) ((Pair) task).getV1();

            if (order.equals("sendTo")) {
                this.target = (String) ((Pair) task).getV2();
            } else if (order.equals("setFocus")) {
                this.focused = (boolean) ((Pair) task).getV2();
            } else if (order.equals("setCurrent")) {
                this.stringElement.setValue((String) ((Pair) task).getV2());
            } else if (order.equals("check") && ((Pair) task).getV2().equals("focus")) {
                if (this.focused) {
                    return true;
                } else {
                    return null;
                }
            }
        } else if (task instanceof Tuple && ((Tuple) task).getV1() instanceof String) {
            if (((Tuple) task).getV1().equals("event")) {
                int key = (int) ((Tuple) task).getV2();
                char c = (char) ((Tuple) task).getV3();
                if (key == Input.KEY_BACK) {
                    this.stringElement.deleteValue(this.stringElement.getValue().length() - 1, 1);
                } else {
                    this.stringElement.addToValue(this.stringElement.getValue().length(), String.valueOf(c));
                }
            }
        }
        return null;
    }

    @Override
    public boolean isActivated() {
        return this.stringElement.isActivated();
    }

    @Override
    public boolean isEmpty() {
        return this.stringElement.isActivated();
    }

    // GETTERS

    @Override
    public Object isOnFocus(float x, float y) {
        if (this.stringElement.isOnFocus(x, y) != null) {
            this.focused = true;
        } else {
            this.focused = false;
            return null;
        }
        return true;
    }

    @Override
    public BodyRect getBody() {
        return this.stringElement.getBody();
    }

    @Override
    public float getAbsoluteWidth() {
        return this.stringElement.getAbsoluteWidth();
    }

    @Override
    public float getAbsoluteHeight() {
        return this.stringElement.getAbsoluteHeight();
    }

    @Override
    public String toString() {
        return this.target + ":" + this.stringElement.toString();
    }
}
