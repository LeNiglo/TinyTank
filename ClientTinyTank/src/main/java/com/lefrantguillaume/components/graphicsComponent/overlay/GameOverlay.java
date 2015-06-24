package com.lefrantguillaume.components.graphicsComponent.overlay;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.ChatElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.EnumInterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 20/06/2015.
 */
public class GameOverlay extends Observable implements Observer {
    private boolean activated;
    private HashMap<EnumInterfaceElement, InterfaceElement> elements;

    public GameOverlay() {
        this.activated = false;
        this.elements = new HashMap<>();
        this.elements.put(EnumInterfaceElement.CHAT, new ChatElement());
    }


    // TASK
    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        Debug.debug("received: " + received);
        if (received.getV2().isIn(EnumTargetTask.GAME_OVERLAY)) {
            if (received.getV3() instanceof MessageChat) {
                this.elements.get(EnumInterfaceElement.CHAT).doTask(received.getV3());
            }
        }
    }

    // FUNCTIONS

    public void draw(Graphics g) {
        if (this.isActivated()) {
            for (Map.Entry entry : this.elements.entrySet()) {
                ((InterfaceElement) entry.getValue()).draw(g);
            }
        }
    }

    public void updateOverlay() {
        for (Map.Entry entry : this.elements.entrySet()) {
            ((InterfaceElement) entry.getValue()).update();
        }
    }

    public Object event(int key, char c) {
        if (this.isActivated()) {
            for (Map.Entry entry : this.elements.entrySet()) {
                Object result = ((InterfaceElement) entry.getValue()).event(key, c);
                if (result instanceof MessageChat) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.MESSAGE_SERVER, result));
                }
                return result;
            }
        }
        return null;
    }

    public boolean isOnFocus(int x, int y) {
        if (this.isActivated()) {
            for (Map.Entry entry : this.elements.entrySet()) {
                if (((InterfaceElement) entry.getValue()).isOnFocus(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void unFocusAll() {
        for (Map.Entry entry : this.elements.entrySet()) {
            ((InterfaceElement) entry.getValue()).setFocused(false);
        }
    }

    public boolean isFocused() {
        if (this.isActivated()) {
            for (Map.Entry entry : this.elements.entrySet()) {
                if (((InterfaceElement) entry.getValue()).isFocused()) {
                    return true;
                }
            }
        }
        return false;
    }

    // GETTERS
    public boolean isActivated() {
        return this.activated;
    }

    // SETTERS
    public void setActivated(boolean value) {
        this.activated = value;
        if (this.activated == false) {
            this.unFocusAll();
        }
    }
}
