package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andres_k on 29/06/2015.
 */
public class ActivatedTimer {
    private Timer timer;
    private ActivatedTask activatedTask;
    private long delay;
    private boolean running;
    private boolean activated;

    public ActivatedTimer(boolean activated) {
        this.activated = activated;
        this.delay = 0;
        this.timer = null;
        this.running = false;
    }

    public ActivatedTimer(boolean activated, boolean run, long delay) {
        this.activated = activated;
        this.delay = delay;
        this.running = false;
        if (run == true) {
            this.startTimer();
        }
    }

    public void leave(){
        if (this.delay != 0){
            if (this.activatedTask != null) {
                this.activatedTask.run();
                this.activatedTask.cancel();
            }
            if (this.timer != null) {
                this.timer.purge();
                this.timer.cancel();
            }
        }
    }

    public void startTimer() {
        if (this.delay != 0) {
            if (this.running == true) {
                this.stopTimer();
                this.startTimer();
            } else {
                this.running = true;
                this.timer = new Timer();
                this.activatedTask = new ActivatedTask();
                this.timer.schedule(this.activatedTask, this.delay);
            }
        } else {
            this.activated = true;
        }
    }

    public void stopTimer() {
        if (this.timer != null) {
            this.running = false;
            this.activatedTask.cancel();
            this.timer.purge();
            this.timer.cancel();
        } else {
            this.activated = false;
        }
    }

    // GETTERS
    public boolean isActivated() {
        return this.activated;
    }

    public boolean isRunning() {
        return this.running;
    }

    // SETTERS
    public void setActivated(boolean value) {
        this.activated = value;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    // TASK

    private class ActivatedTask extends TimerTask {
        @Override
        public void run() {
            activated = !activated;
            running = false;
        }
    }
}
