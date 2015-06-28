package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringListElement extends ListElement {
    private List<BodyRect> positionMessages;
    private int maxLength;
    private int toPrint;

    public StringListElement(BodyRect body) {
        this.body = body;
        this.elements = new ArrayList<>();
        this.toPrint = (int) (body.getSizeY() / 20);
        this.init();
    }

    public StringListElement(BodyRect body, int toPrint) {
        this.body = body;
        this.elements = new ArrayList<>();
        this.toPrint = toPrint;
        this.init();
    }

    private void init() {
        this.maxLength = (int) (body.getSizeX() / 10);
        this.positionMessages = new ArrayList<>();

        this.initPositionMessage();
    }

    // FUNCTIONS
    private void initPositionMessage() {
        int line = 10;
        for (int i = 0; i < this.toPrint; ++i) {
            this.positionMessages.add(0, new BodyRect(new Rectangle(this.body.getX(), this.body.getY() + line, 0, 0)));
            line += 20;
        }
    }

    @Override
    public void draw(Graphics g) {
        int i = 0;

        this.body.draw(g);
        while (i < this.positionMessages.size()) {
            if (i < this.elements.size()) {
                this.elements.get(i).draw(g, this.positionMessages.get(i));
            }
            ++i;
        }
    }

    @Override
    public void update() {
        boolean removed = false;

        for (int i = 0; i < this.elements.size(); ++i) {
            if (this.elements.get(i).isActivated() == false) {
                this.elements.remove(i);
                removed = true;
                --i;
            }
        }
        if (removed == true){
            this.addEmpty();
        }
    }

    @Override
    public void addAllToPrint(List<Object> messageData) {
        this.elements.clear();
        for (int i = 0; i < messageData.size(); ++i) {
            this.addToPrint(messageData.get(i));
        }
        this.addEmpty();
    }

    @Override
    public void addToPrint(Object object) {
        if (object instanceof Pair) {
            Pair<Color, String> message = (Pair<Color, String>) object;
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
    }

    @Override
    public void addToPrint(Object object, long time) {
        if (object instanceof Pair) {
            Pair<Color, String> message = (Pair<Color, String>) object;
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
    }

    private void addMessage(Pair<Color, StringTimer> message) {
        this.elements.add(0, new StringElement(message.getV2(), message.getV1()));
    }

    private void clearEmpty() {
        for (int i = 0; i < this.elements.size(); ++i) {
            if (this.elements.get(i).isEmpty()) {
                this.elements.remove(i);
                --i;
            }
        }
    }

    private void addEmpty() {
        while (this.elements.size() < this.toPrint) {
            this.elements.add(0, new StringElement(new StringTimer(""), Color.black));
        }
    }
}
