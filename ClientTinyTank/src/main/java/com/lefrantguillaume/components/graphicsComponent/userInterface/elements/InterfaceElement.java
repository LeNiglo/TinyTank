package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 23/06/2015.
 */
public abstract class InterfaceElement {
    protected BodyRect body;
    protected ActivatedTimer activatedTimer;
    protected boolean needActivated;
    protected EnumOverlayElement type;

    // FUNCTION
    protected void parentInit(BodyRect body, EnumOverlayElement type, boolean activated, boolean needActivatedParent){
        this.body = body;
        this.activatedTimer = new ActivatedTimer(activated);
        this.type = type;
        this.needActivated = needActivatedParent;
    }

    public void start(){
        this.activatedTimer.startTimer();
    }

    public void stop(){
        this.activatedTimer.stopTimer();
    }

    public abstract void doTask(Object task);

    public abstract void leave();

    public abstract void draw(Graphics g);

    public abstract void update();

    public abstract Object eventPressed(int key, char c);

    public abstract Object eventReleased(int key, char c);

    public abstract boolean isOnFocus(int x, int y);

    // GETTERS
    public boolean isActivated(){
        return this.activatedTimer.isActivated();
    }

    public boolean isNeedActivated(){
        return this.needActivated;
    }

    public EnumOverlayElement getType(){
        return this.type;
    }

    public BodyRect getBody(){
        return this.body;
    }
}
