package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 20/06/2015.
 */
public class ChatElement extends InterfaceElement {
    private List<Tuple<String, String, String>> messageData;
    private List<String> printMessage;
    private List<Rectangle> positionMessage;
    private Rectangle writer;
    private String current;
    private int maxLength;

    public ChatElement() {
        this.messageData = new ArrayList<>();
        this.messageData.add(new Tuple<>("6416541", "Admin", "Welcome!"));
        this.body = new Rectangle(0, WindowConfig.getSizeY() - 200, 400, 200);
        this.maxLength = 40;
        this.current = "";
        this.focused = false;
        this.initMessageBodies();
    }

    public void initMessageBodies() {
        this.printMessage = new ArrayList<>();
        this.addAllToPrint();
        this.positionMessage = new ArrayList<>();
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 10, 0, 0));
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 30, 0, 0));
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 50, 0, 0));
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 70, 0, 0));
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 90, 0, 0));
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 110, 0, 0));
        this.positionMessage.add(new Rectangle(this.body.getMinX(), this.body.getMinY() + 130, 0, 0));
        this.writer = new Rectangle(this.body.getMinX() + 20, this.body.getMinY() + 170, 300, 20);
    }

    // FUNCTIONS

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0.1f, 0.2f, 0.3f, 0.5f));
        g.fill(this.body);
        g.setColor(new Color(0.2f, 0.2f, 0.3f, 0.6f));
        g.fill(this.writer);
        if (this.focused) {
            g.setColor(Color.white);
            g.drawRect(this.writer.getMinX(), this.writer.getMaxY(), this.writer.getWidth(), 1);
        }
        g.setColor(Color.black);
        g.drawString(this.current, this.writer.getMinX(), this.writer.getMinY());
        for (int i = this.positionMessage.size(); i >= 0; --i) {
            if (i < this.positionMessage.size()) {
                g.drawString(this.printMessage.get(i), this.positionMessage.get(i).getMinX(), this.positionMessage.get(i).getMinY());
            }
        }
    }

    @Override
    public void update() {
    }

    @Override
    public Object event(int key, char c) {
        if (key == Input.KEY_ENTER) {
            if (this.focused && !this.current.equals("")) {
                this.focused = false;
                MessageChat request = new MessageChat(CurrentUser.getPseudo(), CurrentUser.getId(), true, this.current);
                this.current = "";
                return request;
            } else if (!this.focused) {
                this.focused = true;
            }
        } else if (this.focused) {
            this.current += c;
        } else if (key == Input.KEY_ESCAPE){
            this.setFocused(false);
        }
        if (this.focused) {
            return false;
        }
        return null;
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof MessageChat) {
            this.addMessage((MessageChat) task);
        }
    }

    public void addAllToPrint() {
        int i = 0;

        this.printMessage.clear();
        while (i < this.messageData.size()) {
            String result = this.getMessageToPrint(this.messageData.get(i));
            int pos = 0;
            int max;
            while (pos < result.length()) {
                max = this.maxLength + pos;
                if (max >= result.length()) {
                    max = result.length();
                }
                String tmp = result.substring(pos, max);
                this.printMessage.add(tmp);
                pos += max;
            }
            ++i;
        }
        while (this.printMessage.size() < 7){
            this.printMessage.add("");
        }
    }

    public void addMessage(MessageChat message) {
        this.messageData.add(new Tuple<>(message.getId(), message.getPseudo(), message.getMessage()));
        int i = 0;
        while (i < this.messageData.size()){
            Debug.debug(this.messageData.get(i).toString());
            ++i;
        }
        this.addAllToPrint();
    }

    public String getMessageToPrint(Tuple<String, String, String> values) {
        return new String(values.getV2() + ": " + values.getV3());
    }
}
