package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringTimer {
    private StringBuffer value;
    private ActivatedTimer activatedTimer;

    public StringTimer(String value) {
        this.value = new StringBuffer(value);
        this.activatedTimer = new ActivatedTimer(true);
    }

    public StringTimer(String value, long time) {
        this.value = new StringBuffer(value);
        this.activatedTimer = new ActivatedTimer(true, true, time);
    }


    // FUNCTIONS

    public void leave(){
        this.activatedTimer.leave();
    }

    public void replace(StringTimer stringTimer) {
        this.value = stringTimer.value;
        this.activatedTimer.stopTimer();
        this.activatedTimer = stringTimer.activatedTimer;
    }

    public void delete(int start, int number) {
        if (start >= 0 && number < this.value.length()) {
            this.value.delete(start, start + number);
        }
    }

    public void add(int position, String value) {
        this.value.insert(position, value);
    }

    // GETTERS
    public String getValue() {
        return this.value.toString();
    }

    public boolean isActivated() {
        return this.activatedTimer.isActivated();
    }

    // SETTERS
    public void setValue(String value) {
        this.value = new StringBuffer(value);
    }
}
