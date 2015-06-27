package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringTimer extends TimerTask {
    private final String value;
    private boolean activated;

    public StringTimer(String value) {
        this.value = value;
        this.activated = true;
    }

    public StringTimer(String value, long time) {
        this.value = value;
        this.activated = true;
        this.createTimer(time);
    }

    private void createTimer(long time) {
        Timer timer = new Timer();
        timer.schedule(this, time);
    }

    // FUNCTIONS
    @Override
    public void run() {
        this.activated = false;
    }

    // GETTERS
    public String getValue() {
        return this.value;
    }

    public boolean isActivated() {
        return this.activated;
    }
}
