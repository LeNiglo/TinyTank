package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.animations.EnumInterfaceComponent;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.graphicsComponent.userInterface.AvailableTank;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessagePlayerNew;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class InterfaceController extends Observable implements Observer {
    private List<Animator> backgroundAnimator;
    private List<Animator> buttonAnimator;
    private AvailableTank availableTank;
    private StateBasedGame stateWindow;

    public InterfaceController(){
        this.backgroundAnimator = new ArrayList<>();
        this.buttonAnimator = new ArrayList<>();
        this.availableTank = new AvailableTank();
        this.stateWindow = null;
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV2().equals(EnumTargetTask.INTERFACE)){

        }
    }

    public boolean loadGame() {
        EnumGameObject tankChoice = this.availableTank.getCurrentTank();
        if (tankChoice != EnumGameObject.NULL) {
            Debug.debug("tank:" + tankChoice);
            MessageModel request = new MessagePlayerNew(CurrentUser.getPseudo(), CurrentUser.getId(), tankChoice);

            this.setChanged();
            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.MESSAGE_SERVER, request));
            return true;
        }
        return false;
    }

    public void drawBackground(Graphics g){
        g.drawAnimation(backgroundAnimator.get(EnumInterfaceComponent.BACKGROUND_1.getIndex()).currentAnimation(), 0, 0);
    }

    public void drawCurrentTankStat(Graphics g){
        this.availableTank.drawCurrentTankStat(g);
    }

    public void changeCurrentTank(int key){
        if (key == Input.KEY_LEFT) {
            this.availableTank.prevTankStat();
        }
        else {
            this.availableTank.nextTankStat();
        }
    }

    public void initAnimator(List<Animator> backgroundAnimator, List<Animator> buttonAnimator, List<Animator> tankStatAnimator, List<Animator> tankPreviewAnimator){
        for (int i = 0; i < backgroundAnimator.size(); ++i) {
            this.addBackgroundAnimator(new Animator(backgroundAnimator.get(i)));
        }
        for (int i = 0; i < buttonAnimator.size(); ++i) {
            this.addButtonAnimator(new Animator(buttonAnimator.get(i)));
        }
        for (int i = 0; i < tankStatAnimator.size(); ++i) {
            this.addTankStatAnimator(new Animator(tankStatAnimator.get(i)));
        }
        for (int i = 0; i < tankPreviewAnimator.size(); ++i) {
            this.addTankPreviewAnimator(new Animator(tankPreviewAnimator.get(i)));
        }
    }

    public void addTankStatAnimator(Animator tankStatAnimator) {
        this.availableTank.addTankStatAnimator(tankStatAnimator);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator) {
        this.availableTank.addTankPreviewAnimator(tankPreviewAnimator);
    }

    public void addBackgroundAnimator(Animator backgroundAnimator) {
        this.backgroundAnimator.add(backgroundAnimator);
    }

    public void addButtonAnimator(Animator buttonAnimator) {
        this.buttonAnimator.add(buttonAnimator);
    }

    //SETTERS
    public void setStateWindow(StateBasedGame stateWindow){
        this.stateWindow = stateWindow;
    }
}
