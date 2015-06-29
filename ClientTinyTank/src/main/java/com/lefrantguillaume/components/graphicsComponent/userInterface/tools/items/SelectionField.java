package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items;

import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Created by andres_k on 29/06/2015.
 */
public class SelectionField {
    private StringElement stringElement;
    private boolean focused;

    public SelectionField(StringElement stringElement){
        this.stringElement = stringElement;
        this.focused = false;
    }

    public void draw(Graphics g){
        if (this.focused == true){
            this.stringElement.draw(g);
        }
    }

    public void draw(Graphics g, boolean mode){
        if (!(mode == true && this.focused == false)) {
            this.stringElement.draw(g);
        }
    }

    public void event(int key, char c){
        if (key == Input.KEY_BACK) {
            this.stringElement.deleteValue(this.stringElement.getValue().length() - 1, 1);
        } else {
            this.stringElement.addToValue(this.stringElement.getValue().length(), String.valueOf(c));
        }
    }

    public void addToCurrent(String value){
        this.stringElement.addToValue(this.stringElement.getValue().length(), value);
    }

    // GETTERS
    public boolean isFocused() {
        return this.focused;
    }

    public boolean isOnFocus(int x, int y){
        if (this.stringElement.isOnFocus(x, y)){
            this.focused = true;
        } else {
            this.focused = false;
        }
        return this.focused;
    }

    public String getCurrent(){
        return this.stringElement.getValue();
    }

    // SETTERS
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public void setCurrent(String value) {
        this.stringElement.setValue(value);
    }
}
