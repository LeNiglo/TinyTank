package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
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

    public StringPopElement(BodyRect body) {
        this.parentInit(body);
        this.childInit();
    }

    // INIT
    @Override
    public void parentInit(BodyRect body) {
        this.focused = false;
        this.needActivated = false;
        this.body = body;
    }

    public void childInit() {
        this.stringListElement = new StringListElement(this.body);
    }

    // FUNCTIONS

    @Override
    public void draw(Graphics g) {
        this.stringListElement.draw(g);
    }

    @Override
    public void update() {
        this.stringListElement.update();
    }

    int i = 0;

    @Override
    public Object event(int key, char c) {
        if (key == Input.KEY_K) {
            this.stringListElement.addToPrint(new Pair<>(Color.red, "test" + i), 3000);
            ++i;
        }
        return null;
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof MessageRoundKill) {
            this.stringListElement.addToPrint(this.getMessageToPrint((MessageRoundKill) task), 3000);
        }
    }

    public Pair<Color, String> getMessageToPrint(MessageRoundKill message) {
        Pair<Color, String> result = new Pair<>(null, null);

        if (CurrentUser.getIdTeam().equals(message.getKiller()))
        {
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
