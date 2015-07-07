package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.StringTools;
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
            this.toPrint = (int) (body.getSizeY() / StringTools.charSizeY());
            this.maxLength = (int) (this.body.getSizeX() / StringTools.charSizeX()) - 1;
            int border = 10;

            int line = border;
            for (int i = 0; i < this.toPrint; ++i) {
                this.positionMessages.add(0, new BodyRect(new Rectangle(this.body.getMinX() + border, this.body.getMinY() + line, this.body.getSizeX() - StringTools.charSizeX(), StringTools.charSizeY())));
                line += StringTools.charSizeY();
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

    private String cleanAllFirst(String value, String suppr) {
        int pos;

        while (value.contains("\n")) {
            pos = value.indexOf("\n");
            if (pos == 0) {
                value = value.replaceFirst(suppr, "");
            } else {
                return value;
            }
        }
        return value;
    }

    private List<StringElement> createMessage(Tuple<Color, String, String> message, Long time, Element.PositionInBody positionInBody) {
        List<StringElement> messages = new ArrayList<>();
        String value = message.getV2();

        while (value.length() != 0) {
            int end;

            value = this.cleanAllFirst(value, "\n");
            if (value.contains("\n")) {
                end = value.indexOf("\n");
                if (end == 0) {
                    Debug.debug("ERROR WARNING");
                } else if (end > this.maxLength) {
                    end = this.maxLength;
                }

            } else {
                end = this.maxLength;
            }
            if (end > value.length()) {
                end = value.length();
            }

            String tmp = value.substring(0, end);
            value = value.substring(end, value.length());

            if (time != null) {
                messages.add(new StringElement(new StringTimer(tmp, time), message.getV1(), message.getV3(), positionInBody));
            } else {
                messages.add(new StringElement(new StringTimer(tmp), message.getV1(), message.getV3(), positionInBody));
            }
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
