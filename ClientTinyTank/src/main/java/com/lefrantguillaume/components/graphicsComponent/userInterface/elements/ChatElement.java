package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
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
    private Rectangle writer;
    private String current;

    public ChatElement(BodyRect body) {
        this.parentInit(body);
        this.childInit();
    }

    // INIT
    @Override
    protected void parentInit(BodyRect body) {
        this.focused = false;
        this.needActivated = true;
        this.body = body;
    }

    private void childInit() {
        this.current = "";
        this.stringListElement = new StringListElement(this.body, 7);
        this.writer = new Rectangle(this.body.getX() + 20, this.body.getY() + 170, 300, 20);
        this.stringListElement.addToPrint(new Pair<>(Color.black, this.getMessageToPrint("admin", "Admin", "Welcome!")));
    }

    // FUNCTIONS
    @Override
    public void draw(Graphics g) {
        this.stringListElement.draw(g);
        g.setColor(new Color(0.2f, 0.2f, 0.3f, 0.6f));
        g.fill(this.writer);
        if (this.focused) {
            g.setColor(Color.white);
            g.drawRect(this.writer.getMinX(), this.writer.getMaxY(), this.writer.getWidth(), 1);
        }
        g.setColor(Color.white);
        int begin = this.current.length() - (int) (this.writer.getWidth() / 10);
        if (begin < 0) {
            begin = 0;
        }
        g.drawString(this.current.substring(begin), this.writer.getMinX(), this.writer.getMinY());
    }

    @Override
    public void update() {
    }

    @Override
    public Object event(int key, char c) {
        if (key == Input.KEY_ENTER) {
            if (this.focused) {
                this.focused = false;
                if (!this.current.equals("")) {
                    MessageChat request = new MessageChat(CurrentUser.getPseudo(), CurrentUser.getId(), true, this.current);
                    this.current = "";
                    return request;
                }
            } else if (!this.focused) {
                this.focused = true;
            }
        } else if (this.focused) {
            this.current += c;
        } else if (key == Input.KEY_ESCAPE) {
            this.setFocused(false);
        } else {
            return null;
        }
        return true;
    }

    @Override
    public Boolean isOnFocus(int x, int y) {
        if (this.stringListElement.isOnFocus(x, y)) {
            this.focused = true;
            return true;
        }
        if (this.body.contains(x, y)) {
            this.focused = true;
            return true;
        }
        this.focused = false;
        return false;
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof MessageChat) {
            this.addMessage((MessageChat) task);
        }
    }

    public void addMessage(MessageChat message) {
        this.stringListElement.addToPrint(new Pair<>(Color.black, this.getMessageToPrint(message.getId(), message.getPseudo(), message.getMessage())));
    }

    public String getMessageToPrint(String id, String pseudo, String message) {
        return pseudo + ": " + message;
    }
}
