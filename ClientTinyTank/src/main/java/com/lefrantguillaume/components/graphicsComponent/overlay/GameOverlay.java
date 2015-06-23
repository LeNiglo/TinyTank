package com.lefrantguillaume.components.graphicsComponent.overlay;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.userInterface.ChatController;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import org.newdawn.slick.Graphics;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 20/06/2015.
 */
public class GameOverlay extends Observable implements Observer{
    private boolean activated;
    private ChatController chatController;
    private ListBox chatBox;

    public GameOverlay(ListBox chatBox) {
        this.chatBox = chatBox;
        this.activated = false;
        this.chatController = new ChatController();
    }


    // TASK
    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV2().isIn(EnumTargetTask.GAME_OVERLAY)) {
            if (received.getV3() instanceof MessageChat) {
                this.chatController.addMessage((MessageChat) received.getV3());
            }
        }
    }

    // NIFTY

    public void bind(Nifty nifty, Screen screen) {
        this.chatBox = screen.findNiftyControl("list-chat", ListBox.class);
        this.chatController.addAllElement(this.chatBox);
    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

    }

    public void updateNifty(Nifty nifty) {
        if (this.isActivated()) {
            nifty.update();
        }
    }

    // FUNCTIONS

    public void draw(Graphics g, Nifty nifty) {
        if (this.isActivated()) {
            nifty.render(false);
        }
    }


    // GETTERS
    public boolean isActivated() {
        return this.activated;
    }

    // SETTERS
    public void setActivated(boolean value) {
        this.activated = value;
        Debug.debug("overlay: " + value);
    }
}
