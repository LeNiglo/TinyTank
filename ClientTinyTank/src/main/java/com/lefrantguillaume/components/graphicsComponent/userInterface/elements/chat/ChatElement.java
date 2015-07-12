package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.chat;

import com.lefrantguillaume.utils.configs.CurrentUser;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.utils.tools.ConsoleWriter;
import com.lefrantguillaume.utils.tools.StringTools;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.SelectionField;
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
        this.selectionField = new SelectionField(new BodyRect(new Rectangle(this.body.getMinX() + 20, this.body.getMinY() + 170, 300, StringTools.charSizeY()), new Color(0.2f, 0.2f, 0.3f, 0.6f)),
                new StringElement(new StringTimer(""), Color.white, Element.PositionInBody.LEFT_MID), EnumOverlayElement.SELECT_FIELD.getValue() + "chat", true);
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
        if (!this.isActivated() && this.selectionFocused()) {
            this.setSelectionFocus(false);
        }
    }

    @Override
    public void clearData() {
        this.stringListElement.clear();
    }

    private boolean selectionFocused(){
        if (this.selectionField.doTask(new Pair<>("check", "focus")) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void setSelectionFocus(boolean value){
        this.selectionField.doTask(new Pair<>("setFocus", value));
    }

    @Override
    public Object eventPressed(int key, char c) {
        if (key == Input.KEY_ENTER) {
            if (this.selectionFocused()) {
                this.setSelectionFocus(false);
                if (!this.selectionField.toString().equals("")) {
                    MessageChat request = new MessageChat(CurrentUser.getPseudo(), CurrentUser.getId(), true, this.selectionField.toString());
                    this.selectionField.doTask(new Pair<>("setCurrent", ""));
                    this.selectionField.doTask(new Pair<>("sendTo", ""));
                    return request;
                }
            } else {
                this.setSelectionFocus(true);
                this.activatedTimer.setActivated(true);
            }
            this.activatedTimer.startTimer();
        } else if (key == Input.KEY_ESCAPE && this.selectionFocused()) {
            return true;
        } else if (this.selectionFocused()) {
            this.selectionField.doTask(new Tuple("event", key, c));
            this.activatedTimer.startTimer();
        } else {
            return null;
        }
        return true;
    }

    @Override
    public Object eventReleased(int key, char c) {
        if (this.selectionFocused()) {
            if (key == Input.KEY_ESCAPE) {
                this.activatedTimer.startTimer();
                this.setSelectionFocus(false);
            }
            return true;
        }
        return null;
    }

    @Override
    public Object isOnFocus(int x, int y) {
        if (this.isActivated()) {
            Object result = this.stringListElement.isOnFocus(x, y);
            if (result instanceof Element) {
                //todo catach l'element et l'envoyer au selectField pour envois de message by id
                ConsoleWriter.debug("element CATCH");
                this.selectionField.doTask(new Pair<>("sendTo", ((Element) result).toString()));
                return result;
            }
            if (this.selectionField.isOnFocus(x, y) != null) {
                ConsoleWriter.debug("selection CATCH");
                return true;
            }
        }
        return null;
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