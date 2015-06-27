package com.lefrantguillaume.components.graphicsComponent.userInterface.tools;

import com.lefrantguillaume.Utils.stockage.Pair;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringListElement extends Element {
    private List<Pair<Color, StringTimer>> printMessages;
    private List<Rectangle> positionMessages;
    private int maxLength;
    private int toPrint;

    public StringListElement(BodyRect body) {
        this.body = body;
        this.toPrint = (int) (body.getSizeY() / 20);
        this.init();
    }

    public StringListElement(BodyRect body, int toPrint) {
        this.body = body;
        this.toPrint = toPrint;
        this.init();
    }

    private void init() {
        this.maxLength = (int) (body.getSizeX() / 10);
        this.printMessages = new ArrayList<>();
        this.positionMessages = new ArrayList<>();

        this.initPositionMessage();
    }

    // FUNCTIONS
    private void initPositionMessage() {
        int line = 10;
        for (int i = 0; i < this.toPrint; ++i) {
            this.positionMessages.add(0, new Rectangle(this.body.getX(), this.body.getY() + line, 0, 0));
            line += 20;
        }
    }

    @Override
    public void draw(Graphics g) {
        int i = 0;

        this.body.draw(g);
        while (i < this.positionMessages.size()) {
            if (i < this.printMessages.size()) {
                g.setColor(this.printMessages.get(i).getV1());
                g.drawString(this.printMessages.get(i).getV2().getValue(), this.positionMessages.get(i).getMinX(), this.positionMessages.get(i).getMinY());
            }
            ++i;
        }
    }

    @Override
    public void update() {
        boolean removed = false;

        for (int i = 0; i < this.printMessages.size(); ++i) {
            if (this.printMessages.get(i).getV2().isActivated() == false) {
                this.printMessages.remove(i);
                removed = true;
                --i;
            }
        }
        if (removed == true){
            this.addEmpty();
        }
    }

    public void addAllToPrint(List<Pair<Color, String>> messageData) {
        this.printMessages.clear();
        for (int i = 0; i < messageData.size(); ++i) {
            this.addToPrint(messageData.get(i));
        }
        this.addEmpty();
    }

    public void addToPrint(Pair<Color, String> message) {
        int pos = 0;
        int max;
        this.clearEmpty();
        while (pos < message.getV2().length()) {
            max = this.maxLength + pos;
            if (max >= message.getV2().length()) {
                max = message.getV2().length();
            }
            String tmp = message.getV2().substring(pos, max);
            this.addMessage(new Pair<>(message.getV1(), new StringTimer(tmp)));
            pos += max;
        }
        this.addEmpty();
    }

    public void addToPrint(Pair<Color, String> message, long time) {
        int pos = 0;
        int max;
        this.clearEmpty();
        while (pos < message.getV2().length()) {
            max = this.maxLength + pos;
            if (max >= message.getV2().length()) {
                max = message.getV2().length();
            }
            String tmp = message.getV2().substring(pos, max);
            this.addMessage(new Pair<>(message.getV1(), new StringTimer(tmp, time)));
            pos += max;
        }
        this.addEmpty();
    }

    private void addMessage(Pair<Color, StringTimer> message) {
        this.printMessages.add(0, message);
    }

    private void clearEmpty() {
        for (int i = 0; i < this.printMessages.size(); ++i) {
            if (this.printMessages.get(i).getV2().getValue().equals("")) {
                this.printMessages.remove(i);
                --i;
            }
        }
    }

    private void addEmpty() {
        while (this.printMessages.size() < this.toPrint) {
            this.printMessages.add(0, new Pair<>(Color.black, new StringTimer("")));
        }
    }
}
