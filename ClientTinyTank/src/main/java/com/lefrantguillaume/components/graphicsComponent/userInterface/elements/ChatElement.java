package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.SelectionField;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.ListElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.StringListElement;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 20/06/2015.
 */

public class ChatElement extends InterfaceElement {
    private ListElement stringListElement;
    private SelectionField selectionField;

    public ChatElement(EnumOverlayElement type, BodyRect body) {
        this.parentInit(body, type, false, new boolean[]{true, true});
        this.childInit();
    }

    // INIT
    @Override
    protected void parentInit(BodyRect body, EnumOverlayElement type, boolean activated, boolean[] needActivatedParent) {
        this.body = body;
        this.reachable = needActivatedParent;
        this.activatedTimer = new ActivatedTimer(activated, false, 7000);
        this.type = type;
    }

    private void childInit() {
        this.selectionField = new SelectionField(new StringElement(new BodyRect(new Rectangle(this.body.getMinX() + 20, this.body.getMinY() + 170, 300, 20), new Color(0.2f, 0.2f, 0.3f, 0.6f)),
                new StringTimer(""), Color.white, Element.PositionInBody.LEFT_MID));
        float chatSizeY = 170;
        this.stringListElement = new StringListElement(new BodyRect(new Rectangle(this.body.getMinX(), this.body.getMinY(), this.body.getSizeX(), chatSizeY)));
    }

    // FUNCTIONS
    @Override
    public void leave() {
        this.activatedTimer.leave();
        this.stringListElement.leave();
        this.stringListElement.clear();
    }

    @Override
    public void draw(Graphics g) {
        if (this.isActivated()) {
            this.body.draw(g);
            this.stringListElement.draw(g);
            this.selectionField.draw(g);
        }
    }

    @Override
    public void update() {
        if (!this.isActivated() && this.selectionField.isFocused()) {
            this.selectionField.setFocused(false);
        }
    }

    @Override
    public Object eventPressed(int key, char c) {
        if (key == Input.KEY_ENTER) {
            if (this.selectionField.isFocused()) {
                this.selectionField.setFocused(false);
                if (!this.selectionField.getCurrent().equals("")) {
                    MessageChat request = new MessageChat(CurrentUser.getPseudo(), CurrentUser.getId(), true, this.selectionField.getCurrent());
                    this.selectionField.setCurrent("");
                    return request;
                }
            } else {
                this.selectionField.setFocused(true);
                this.activatedTimer.setActivated(true);
            }
            this.activatedTimer.startTimer();
        } else if (key == Input.KEY_ESCAPE && this.selectionField.isFocused()) {
            return true;
        } else if (this.selectionField.isFocused()) {
            this.selectionField.event(key, c);
            this.activatedTimer.startTimer();
        } else {
            return null;
        }
        return true;
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (this.selectionField.isFocused()) {
            if (key == Input.KEY_ESCAPE) {
                this.activatedTimer.startTimer();
                this.selectionField.setFocused(false);
            }
            return true;
        }
        return null;
    }

    @Override
    public boolean isOnFocus(int x, int y) {
        if (this.isActivated()) {
            Object result = this.stringListElement.isOnFocus(x, y);
            if (result instanceof Element) {
                //todo catach l'element et l'envoyer au selectField pour envois de message by id
                Debug.debug("element CATCH");
                this.selectionField.addToCurrent(((Element) result).getId());
                return true;
            }
            if (this.selectionField.isOnFocus(x, y)) {
                Debug.debug("selection CATCH");
                return true;
            }
        }
        return false;
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof MessageChat) {
            this.activatedTimer.setActivated(true);
            this.addMessage((MessageChat) task);
            this.activatedTimer.startTimer();
        } else if (task instanceof Pair) {
            Pair<Integer, Boolean> received = (Pair<Integer, Boolean>) task;
            if (received.getV1() < this.reachable.length) {
                this.reachable[received.getV1()] = received.getV2();
            }
        }
    }

    public void addMessage(MessageChat message) {
        this.stringListElement.addToPrint(new Tuple<>(Color.black, this.getMessageToPrint(message.getPseudo(), message.getMessage()), message.getId()), Element.PositionInBody.LEFT_MID);
    }

    public String getMessageToPrint(String pseudo, String message) {
        return pseudo + ": " + message;
    }
}