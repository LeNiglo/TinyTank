package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
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
        this.positionMessages = new ArrayList<>();
    }

    public StringListElement(BodyRect body) {
        this.body = body;
        this.elements = new ArrayList<>();
        this.positionMessages = new ArrayList<>();
        this.updatePosition();
    }

    // FUNCTIONS
    @Override
    protected void updatePosition() {
        if (this.body != null) {
            this.toPrint = (int) (body.getSizeY() / 20);
            this.maxLength = (int) (this.body.getSizeX() / 10) - 1;

            int line = 10;
            for (int i = 0; i < this.toPrint; ++i) {
                this.positionMessages.add(0, new BodyRect(new Rectangle(this.body.getMinX() + 10, this.body.getMinY() + line, this.body.getSizeX() - 10, 20)));
                line += 20;
            }
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
        if (removed == true) {
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
            this.addMessage((Tuple<Color, String, String>) object, null, positionInBody, 0);
        } else if (object instanceof StringElement) {
            StringElement task = (StringElement) object;

            int position = this.deleteElemIfExist(task.getId());
            this.addMessage(new Tuple<>(task.getColor(), task.getValue(), task.getId()), null, positionInBody, position);
        }
    }

    @Override
    public void addToPrint(Object object, long time, Element.PositionInBody positionInBody) {
        if (object instanceof Tuple) {
            this.addMessage((Tuple<Color, String, String>) object, time, positionInBody, 0);
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
        if (this.body != null && this.body.isOnFocus(x, y)) {
            return null;
        }
        return null;
    }

    private int deleteElemIfExist(String id) {
        this.clearEmpty();
        int position = this.containsElement(id);
        if (position != -1) {
            for (int i = position; i < this.elements.size(); ++i) {
                if (this.containsHeadId(elements.get(i).getId(), id)) {
                    this.elements.remove(i);
                    --i;
                    if (position > 0) {
                        --position;
                    }
                }
            }
            return position;
        } else {
            return 0;
        }
    }

    private List<StringElement> createMessage(Tuple<Color, String, String> message, Long time, Element.PositionInBody positionInBody) {
        List<StringElement> messages = new ArrayList<>();
        int posInString = 0;
        int max;

        Debug.debug("ADD Message: " + message.getV2());
        while (posInString < message.getV2().length()) {
            max = this.maxLength + posInString;
            if (max >= message.getV2().length()) {
                max = message.getV2().length();
            }
            String tmp = message.getV2().substring(posInString, max);
            if (time != null) {
                messages.add(new StringElement(new StringTimer(tmp, time), message.getV1(), message.getV3(), positionInBody));
            } else {
                messages.add(new StringElement(new StringTimer(tmp), message.getV1(), message.getV3(), positionInBody));
            }
            posInString += max;
        }
        return messages;
    }

    private void addMessage(Tuple<Color, String, String> message, Long time, Element.PositionInBody positionInBody, int position) {
        List<StringElement> messages = this.createMessage(message, time, positionInBody);

        this.clearEmpty();
        for (StringElement element : messages) {
            this.elements.add(position, element);
        }
        this.addEmpty();
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
