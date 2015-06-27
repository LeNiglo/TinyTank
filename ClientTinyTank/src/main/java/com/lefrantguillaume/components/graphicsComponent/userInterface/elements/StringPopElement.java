package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.StringListBody;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageRoundKill;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 20/06/2015.
 */
public class StringPopElement extends InterfaceElement {
    private StringListBody stringListBody;

    public StringPopElement(Rectangle body) {
        this.parentInit(body);
        this.childInit();
    }

    @Override
    public void parentInit(Rectangle body) {
        this.focused = false;
        this.needActivated = false;
        this.body = body;
    }

    public void childInit() {
        this.stringListBody = new StringListBody(this.body);
    }

    // FUNCTIONS

    @Override
    public void draw(Graphics g) {
        this.stringListBody.draw(g);
    }

    @Override
    public void update() {
        this.stringListBody.update();
    }

    int i = 0;

    @Override
    public Object event(int key, char c) {
        if (key == Input.KEY_K) {
            this.stringListBody.addToPrint(new Pair<>(Color.red, "test" + i), 3000);
            ++i;
        }
        return null;
    }

    @Override
    public void doTask(Object task) {
        if (task instanceof MessageRoundKill) {
            this.stringListBody.addToPrint(this.getMessageToPrint((MessageRoundKill) task), 3000);
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
