package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.animations.EnumInterfaceComponent;
import com.lefrantguillaume.interfaceComponent.AvailableTank;
import com.lefrantguillaume.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePlayerNew;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

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

    public InterfaceController(){
        this.backgroundAnimator = new ArrayList<Animator>();
        this.buttonAnimator = new ArrayList<Animator>();
        this.availableTank = new AvailableTank();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public boolean loadGame() {
        EnumTanks tankChoice = this.availableTank.getCurrentTank();
        if (tankChoice != EnumTanks.NULL) {
            Debug.debug("tank:" + tankChoice);
            MessageModel request = new MessagePlayerNew(CurrentUser.getPseudo(), CurrentUser.getId(), tankChoice);

            this.setChanged();
            this.notifyObservers(request);
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
}
