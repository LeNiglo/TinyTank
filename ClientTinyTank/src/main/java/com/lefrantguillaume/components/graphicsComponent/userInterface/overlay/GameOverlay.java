package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.Utils.stockage.Tuple;
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

        if (received.getV2().isIn(EnumTargetTask.GAME_OVERLAY)) {
            if (received.getV3() instanceof MessageChat) {
                this.elements.get(EnumInterfaceElement.CHAT).doTask(received.getV3());
            }
        }
    }

    // FUNCTIONS

    public void draw(Graphics g) {
        for (Map.Entry<EnumInterfaceElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                entry.getValue().draw(g);
            }
        }
    }

    public void updateOverlay() {
        for (Map.Entry<EnumInterfaceElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                entry.getValue().update();
            }
        }
    }


    public Object event(int key, char c) {
        for (Map.Entry<EnumInterfaceElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                Object result = entry.getValue().event(key, c);
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
        for (Map.Entry<EnumInterfaceElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                if (entry.getValue().isOnFocus(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void unFocusAll() {
        for (Map.Entry<EnumInterfaceElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().setFocused(false);
        }
    }

    public boolean isFocused() {
        for (Map.Entry<EnumInterfaceElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                if (entry.getValue().isFocused()) {
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