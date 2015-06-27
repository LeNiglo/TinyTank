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
public class StringListBody {
    private List<Pair<Color, StringTimer>> printMessages;
    private List<Rectangle> positionMessages;
    private Rectangle body;
    private int maxLength;
    private int toPrint;

    public StringListBody(Rectangle body, int toPrint){
        this.toPrint = toPrint;
        this.body = body;
        this.maxLength = (int) (body.getWidth() / 10);
        this.printMessages = new ArrayList<>();
        this.positionMessages = new ArrayList<>();

        this.initPositionMessage();
    }

    // FUNCTIONS
    private void initPositionMessage(){
        int line = 10;
        for (int i = 0; i < this.toPrint; ++i){
            this.positionMessages.add(0, new Rectangle(this.body.getMinX(), this.body.getMinY() + line, 0, 0));
            line += 20;
        }
    }

    public void draw(Graphics g){
        int i = 0;

        while (i < this.positionMessages.size()){
            if (i < this.printMessages.size()){
                g.setColor(this.printMessages.get(i).getV1());
                g.drawString(this.printMessages.get(i).getV2().getValue(), this.positionMessages.get(i).getMinX(), this.positionMessages.get(i).getMinY());
            }
            ++i;
        }
        g.setColor(Color.black);
    }

    public void update(){
        for (int i = 0; i < this.printMessages.size(); ++i){
            if (this.printMessages.get(i).getV2().isActivated() == false){
                this.printMessages.remove(i);
                --i;
            }
        }
    }

    public void addAllToPrint(List<Pair<Color, String>> messageData) {
        this.printMessages.clear();
        for (int i = 0; i < messageData.size(); ++i) {
            this.addToPrint(messageData.get(i));
        }
        while (this.printMessages.size() < this.toPrint){
            this.addMessage(new Pair<>(Color.black, new StringTimer("")));
        }
    }

    public void addAllToPrint(List<Pair<Color, String>> messageData, long time) {
        this.printMessages.clear();
        for (int i = 0; i < messageData.size(); ++i) {
            this.addToPrint(messageData.get(i), time);
        }
        while (this.printMessages.size() < this.toPrint){
            this.addMessage(new Pair<>(Color.black, new StringTimer("", time)));
        }
    }

    public void addToPrint(Pair<Color, String> message){
        int pos = 0;
        int max;
        while (pos < message.getV2().length()) {
            max = this.maxLength + pos;
            if (max >= message.getV2().length()) {
                max = message.getV2().length();
            }
            String tmp = message.getV2().substring(pos, max);
            this.addMessage(new Pair<>(message.getV1(), new StringTimer(tmp)));
            pos += max;
        }
    }

    public void addToPrint(Pair<Color, String> message, long time){
        int pos = 0;
        int max;
        while (pos < message.getV2().length()) {
            max = this.maxLength + pos;
            if (max >= message.getV2().length()) {
                max = message.getV2().length();
            }
            String tmp = message.getV2().substring(pos, max);
            this.addMessage(new Pair<>(message.getV1(), new StringTimer(tmp, time)));
            pos += max;
        }
    }

    private void addMessage(Pair<Color, StringTimer> message){
        this.printMessages.add(0, message);
    }
}
