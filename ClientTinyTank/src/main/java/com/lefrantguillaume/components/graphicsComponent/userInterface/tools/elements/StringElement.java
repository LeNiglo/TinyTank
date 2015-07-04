package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public class StringElement extends Element {
    private StringTimer stringTimer;
    private Color color;

    public StringElement(StringTimer stringTimer, Color color, PositionInBody position) {
        this.init(null, "", position, EnumOverlayElement.STRING);
        this.stringTimer = stringTimer;
        this.color = color;
    }

    public StringElement(StringTimer stringTimer, Color color, String id, PositionInBody position) {
        this.init(null, id, position, EnumOverlayElement.STRING);
        this.stringTimer = stringTimer;
        this.color = color;
    }

    public StringElement(BodyRect body, StringTimer stringTimer, Color color, PositionInBody position) {
        this.init(body, "", position, EnumOverlayElement.STRING);
        this.stringTimer = stringTimer;
        this.color = color;
    }

    public StringElement(BodyRect body, StringTimer stringTimer, Color color, String id, PositionInBody position) {
        this.init(body, id, position, EnumOverlayElement.STRING);
        this.stringTimer = stringTimer;
        this.color = color;
    }


    @Override
    public void leave() {
        this.stringTimer.leave();
    }

    public void draw(Graphics g) {
        if (this.body != null) {
            int begin = this.stringTimer.getValue().length() - (int) (this.body.getSizeX() / StringTools.charSizeX());
            begin = (begin < 0 ? 0 : begin);

            String value = this.stringTimer.getValue().substring(begin);

            Pair<Float, Float> position = this.getChoicePosition(this.body, value);

            this.body.draw(g);
            g.setColor(this.color);
            g.drawString(value, position.getV1(), position.getV2());
        }
    }

    @Override
    public void draw(Graphics g, BodyRect body) {
        int begin = this.stringTimer.getValue().length() - (int) (body.getSizeX() / StringTools.charSizeX());
        begin = (begin < 0 ? 0 : begin);

        String value = this.stringTimer.getValue().substring(begin);

        Pair<Float, Float> position = this.getChoicePosition(body, value);

        if (this.body != null && body.getColor() == null){
            body.setColor(this.body.getColor());
        }

        body.draw(g);
  /*
        if (this.body != null && body.getColor() == null) {
            this.body = new BodyRect(body.getBody(), this.body.getColor());
        }
        else {
            body.draw(g);
        }
        */
        g.setColor(this.color);
        g.drawString(value, position.getV1(), position.getV2());
    }

    private Pair<Float, Float> getChoicePosition(BodyRect body, String value) {
        float x = body.getMinX();
        float y = body.getMinY();

        if (this.position == PositionInBody.MIDDLE_MID || this.position == PositionInBody.MIDDLE_UP) {
            float sizeX = (body.getSizeX() / 2) - ((value.length() * StringTools.charSizeX()) / 2);

            sizeX = (sizeX < 0 ? 0 : sizeX);
            x += sizeX;
        } else if (this.position == PositionInBody.RIGHT_MID || this.position == PositionInBody.RIGHT_UP) {
            float sizeX = (body.getSizeX() - (value.length() * StringTools.charSizeX()));

            sizeX = (sizeX < 0 ? 0 : sizeX);
            x += sizeX;
        }
        return new Pair<>(x, y);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean replace(Element element) {
        if (element.getType() == EnumOverlayElement.STRING) {
            StringElement newElement = (StringElement) element;

            this.stringTimer.replace(newElement.stringTimer);
            this.color = newElement.color;
            return true;
        }
        return false;
    }

    @Override
    public Object doTask(Object task) {
        if (this.stringTimer.getValue().contains(":")) {
            String v1 = this.stringTimer.getValue().substring(0, this.stringTimer.getValue().indexOf(":") + 1);
            String v2 = (String) task;
            this.stringTimer.setValue(v1 + v2);
            return true;
        }
        return null;
    }

    public void addToValue(int position, String add) {
        this.stringTimer.add(position, add);
    }

    public void deleteValue(int start, int number) {
        this.stringTimer.delete(start, number);
    }

    // GETTERS
    @Override
    public boolean isActivated() {
        return this.stringTimer.isActivated();
    }

    @Override
    public boolean isEmpty() {
        return this.stringTimer.getValue().equals("");
    }

    @Override
    public float getAbsoluteWidth() {
        return this.stringTimer.getValue().length() * StringTools.charSizeX();
    }

    @Override
    public float getAbsoluteHeight() {
        return StringTools.charSizeY();
    }

    public String getValue() {
        return this.stringTimer.getValue();
    }

    public Color getColor() {
        return this.color;
    }

    // SETTERS
    public void setValue(String value) {
        this.stringTimer.setValue(value);
    }

    @Override
    public String toString() {
        return this.stringTimer.getValue();
    }
}
