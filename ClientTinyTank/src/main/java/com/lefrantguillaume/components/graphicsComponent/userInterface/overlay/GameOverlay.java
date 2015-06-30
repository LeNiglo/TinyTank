package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.*;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ImageElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 20/06/2015.
 */
public class GameOverlay extends Observable implements Observer {
    private boolean activated;
    private HashMap<EnumOverlayElement, InterfaceElement> elements;

    public GameOverlay() {
        this.activated = false;
        this.elements = new HashMap<>();
        this.elements.put(EnumOverlayElement.CHAT, new ChatElement(EnumOverlayElement.CHAT, new BodyRect(new Rectangle(0, WindowConfig.getSizeY() - 200, 400, 200), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
        this.elements.put(EnumOverlayElement.POP_KILL, new StringPopElement(EnumOverlayElement.POP_KILL, new BodyRect(new Rectangle(WindowConfig.getSizeX() - 250, 0, 250, 400))));
        this.elements.put(EnumOverlayElement.TABLE_NEW_ROUND, new TableElement(EnumOverlayElement.TABLE_NEW_ROUND, new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 368, (WindowConfig.getSizeY() / 2) - 72, 700, 300), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
        this.elements.put(EnumOverlayElement.TABLE_STAT, new TableElement(EnumOverlayElement.TABLE_STAT, new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 368, (WindowConfig.getSizeY() / 2) - 72, 700, 300), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
    }

    public void init(AnimatorOverlayData animatorOverlayData) {
        InterfaceElement tableNewRound = this.elements.get(EnumOverlayElement.TABLE_NEW_ROUND);

        tableNewRound.doTask(new ImageElement(animatorOverlayData.getRoundAnimator(EnumOverlayElement.NEW_ROUND), "newRound", Element.PositionInBody.MIDDLE_UP));
        tableNewRound.doTask(new ImageElement(animatorOverlayData.getRoundAnimator(EnumOverlayElement.STATE), "newRound", Element.PositionInBody.MIDDLE_MID));
    }

    // TASK
    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        Debug.debug("RECEIVED : " + received);
        if (received.getV2().isIn(EnumTargetTask.GAME_OVERLAY)) {
            for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
                entry.getValue().doTask(received.getV3());
            }
        }
    }

    // FUNCTIONS

    public void draw(Graphics g) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                entry.getValue().draw(g);
            }
        }
    }

    public void updateOverlay() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                entry.getValue().update();
            }
        }
    }


    public boolean event(int key, char c, EnumInput type) {
        boolean used = false;

        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                Object result = null;
                if (type == EnumInput.PRESSED) {
                    result = entry.getValue().eventPressed(key, c);
                } else if (type == EnumInput.RELEASED) {
                    result = entry.getValue().eventReleased(key, c);
                }
                if (result instanceof MessageChat) {
                    used = true;
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.MESSAGE_SERVER, result));
                } else if (result instanceof Boolean) {
                    used = (Boolean) result;
                }
            }
        }
        return used;
    }

    public boolean isOnFocus(int x, int y) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                if (entry.getValue().isOnFocus(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFocused() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            if (!(this.isActivated() == false && entry.getValue().isNeedActivated())) {
                if (entry.getValue().isActivated()) {
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
    }
}
