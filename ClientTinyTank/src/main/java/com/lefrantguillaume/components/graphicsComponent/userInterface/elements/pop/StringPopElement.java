package com.lefrantguillaume.components.graphicsComponent.userInterface.elements.pop;

import com.lefrantguillaume.utils.configs.CurrentUser;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.StringListElement;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundKill;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Created by andres_k on 20/06/2015.
 */
public class StringPopElement extends InterfaceElement {
    private StringListElement stringListElement;

    public StringPopElement(EnumOverlayElement type, BodyRect body) {
        this.parentInit(body, type, true, new boolean[]{true, true});
        this.childInit();
    }

    // INIT

    public void childInit() {
        this.stringListElement = new StringListElement(this.body);
    }

    // FUNCTIONS

    @Override
    public void leave() {
        this.stringListElement.leave();
        this.activatedTimer.leave();
    }

    @Override
    public void draw(Graphics g) {
        if (this.isActivated()) {
            this.stringListElement.draw(g);
        }
    }

    @Override
    public void update() {
        this.stringListElement.update();
    }

    @Override
    public void clearData() {
        this.stringListElement.clear();
    }

    int i = 0;

    @Override
    public Object eventPressed(int key, char c) {
        if (key == Input.KEY_K) {
            this.stringListElement.addToPrint(new Tuple<>(Color.red, "test" + i, "overlay"), 3000, Element.PositionInBody.MIDDLE_MID);
            ++i;
        }
        return null;
    }

    @Override
    public Object eventReleased(int key, char c) {
        return null;
    }

    @Override
    public Object isOnFocus(int x, int y) {
        if (this.isActivated()) {
            if (this.stringListElement.isOnFocus(x, y) != null) {
            }
            if (this.body.isOnFocus(x, y)) {
            }
        }
        return null;
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof MessageRoundKill) {
            this.stringListElement.addToPrint(this.getMessageToPrint((MessageRoundKill) task), 3000, Element.PositionInBody.MIDDLE_MID);
        } else if (task instanceof Pair) {
            Pair<Integer, Boolean> received = (Pair<Integer, Boolean>) task;
            if (received.getV1() < this.reachable.length) {
                this.reachable[received.getV1()] = received.getV2();
            }
        }
    }

    public Tuple<Color, String, String> getMessageToPrint(MessageRoundKill message) {
        Tuple<Color, String, String> result = new Tuple<>(null, null, "overlay");

        if (CurrentUser.getIdTeam().equals(message.getKiller())) {
            result.setV1(Color.cyan);
        } else {
            result.setV1(Color.red);
        }
        if (message.getKillerTeam().equals(message.getTargetTeam())) {
            result.setV2(message.getKiller() + " killed is ally " + message.getTarget() + " *shame on him!*");
        } else {
            result.setV2(message.getKiller() + " killed " + message.getTarget());
        }
        return result;
    }
}
