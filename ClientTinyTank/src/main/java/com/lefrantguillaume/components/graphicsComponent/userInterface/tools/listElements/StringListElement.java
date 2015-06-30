package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
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

    public StringListElement() {
        this.body = null;
        this.elements = new ArrayList<>();
    }

    public StringListElement(BodyRect body) {
        this.body = body;
        this.elements = new ArrayList<>();
        this.init((int) (body.getSizeY() / 20));
    }

    public StringListElement(BodyRect body, int toPrint) {
        this.body = body;
        this.elements = new ArrayList<>();
        this.toPrint = toPrint;
        this.init(toPrint);
    }

    private void init(int toPrint) {
        if (this.body != null) {
            this.toPrint = toPrint;
            this.maxLength = (int) (body.getSizeX() / 10);
            this.positionMessages = new ArrayList<>();

            this.updatePosition();
        }
    }

    // FUNCTIONS
    @Override
    protected void updatePosition() {
        int line = 10;
        for (int i = 0; i < this.toPrint; ++i) {
            this.positionMessages.add(0, new BodyRect(new Rectangle(this.body.getMinX() + 10, this.body.getMinY() + line, this.body.getSizeX() - 10, 20)));
            line += 20;
        }
    }

    @Override
    public void draw(Graphics g) {
        int i = 0;

        if (this.body != null) {
            this.body.draw(g);
            while (i < this.positionMessages.size()) {
                if (i < this.elements.size()) {
                    this.elements.get(i).draw(g, this.positionMessages.get(i));
                }
                ++i;
            }
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
    public void addAllToPrint(List<Object> messageData, Element.PositionInBody positionInBody) {
        this.elements.clear();
        for (int i = 0; i < messageData.size(); ++i) {
            this.addToPrint(messageData.get(i), positionInBody);
        }
        this.addEmpty();
    }

    @Override
    public void addToPrint(Object object, Element.PositionInBody positionInBody) {
        if (object instanceof Tuple) {
            Tuple<Color, String, String> message = (Tuple<Color, String, String>) object;
            int pos = 0;
            int max;
            this.clearEmpty();
            while (pos < message.getV2().length()) {
                max = this.maxLength + pos;
                if (max >= message.getV2().length()) {
                    max = message.getV2().length();
                }
                String tmp = message.getV2().substring(pos, max);
                this.addMessage(new Pair<>(message.getV1(), new StringTimer(tmp)), message.getV3(), positionInBody);
                pos += max;
            }
            this.addEmpty();
        }
    }

    @Override
    public void addToPrint(Object object, long time, Element.PositionInBody positionInBody) {
        if (object instanceof Tuple) {
            Tuple<Color, String, String> message = (Tuple<Color, String, String>) object;
            int pos = 0;
            int max;
            this.clearEmpty();
            while (pos < message.getV2().length()) {
                max = this.maxLength + pos;
                if (max >= message.getV2().length()) {
                    max = message.getV2().length();
                }
                String tmp = message.getV2().substring(pos, max);
                this.addMessage(new Pair<>(message.getV1(), new StringTimer(tmp, time)), message.getV3(), positionInBody);
                pos += max;
            }
            this.addEmpty();
        }
    }

    @Override
    public Object isOnFocus(float x, float y) {
        for (int i = 0; i < this.positionMessages.size(); ++i) {
            if (i < this.elements.size() && this.positionMessages.get(i).isOnFocus(x, y)) {
                if (this.elements.get(i).isEmpty() == false) {
                    return elements.get(i);
                }
            }
        }
        if (this.body != null && this.body.isOnFocus(x, y)){
            return null;
        }
        return null;
    }

    private void addMessage(Pair<Color, StringTimer> message, String id, Element.PositionInBody positionInBody) {
        this.elements.add(0, new StringElement(message.getV2(), message.getV1(), id, positionInBody));
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
            this.elements.add(0, new StringElement(new StringTimer(""), Color.black, Element.PositionInBody.LEFT_MID));
        }
    }
}
