package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.graphicsComponent.userInterface.AvailableTank;
import com.lefrantguillaume.components.graphicsComponent.userInterface.EnumInterfaceComponent;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessagePlayerNew;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;

/**
 * Created by andres_k on 13/03/2015.
 */
public class InterfaceController extends Observable implements Observer {
    private HashMap<EnumInterfaceComponent, Animator> backgroundAnimator;
    private HashMap<EnumInterfaceComponent, Animator> buttonAnimator;
    private AvailableTank availableTank;
    private StateBasedGame stateWindow;

    public InterfaceController(){
        this.backgroundAnimator = new HashMap<>();
        this.buttonAnimator = new HashMap<>();
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
        g.drawAnimation(backgroundAnimator.get(EnumInterfaceComponent.BACKGROUND_1).currentAnimation(), 0, 0);
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

    public void initAnimator(HashMap<EnumInterfaceComponent, Animator> backgroundAnimator, HashMap<EnumInterfaceComponent, Animator> buttonAnimator,
                             HashMap<EnumInterfaceComponent, Animator> tankStatAnimator, HashMap<EnumInterfaceComponent, Animator> tankPreviewAnimator){
        for (Map.Entry entry : backgroundAnimator.entrySet()){
            this.addBackgroundAnimator(new Animator((Animator)entry.getValue()), (EnumInterfaceComponent) entry.getKey());
        }
        for (Map.Entry entry : buttonAnimator.entrySet()){
            this.addButtonAnimator(new Animator((Animator)entry.getValue()), (EnumInterfaceComponent) entry.getKey());
        }
        for (Map.Entry entry : tankStatAnimator.entrySet()){
            this.addTankStatAnimator(new Animator((Animator)entry.getValue()), (EnumInterfaceComponent) entry.getKey());
        }
        for (Map.Entry entry : tankPreviewAnimator.entrySet()){
            this.addTankPreviewAnimator(new Animator((Animator)entry.getValue()), (EnumInterfaceComponent) entry.getKey());
        }
    }

    public void addTankStatAnimator(Animator tankStatAnimator, EnumInterfaceComponent type) {
        this.availableTank.addTankStatAnimator(tankStatAnimator, type);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator, EnumInterfaceComponent type) {
        this.availableTank.addTankPreviewAnimator(tankPreviewAnimator, type);
    }

    public void addBackgroundAnimator(Animator backgroundAnimator, EnumInterfaceComponent type) {
        this.backgroundAnimator.put(type, backgroundAnimator);
    }

    public void addButtonAnimator(Animator buttonAnimator, EnumInterfaceComponent type) {
        this.buttonAnimator.put(type, buttonAnimator);
    }

    //SETTERS
    public void setStateWindow(StateBasedGame stateWindow){
        this.stateWindow = stateWindow;
    }
}
