package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 17/03/2015.
 */
public class Windows extends StateBasedGame implements Observer {

    GenericSendTask gameTask;
    GenericSendTask inputTask;
    GenericSendTask interfaceTask;
    GenericSendTask accountTask;
    GenericSendTask masterTask;

    public Windows(String name, GenericSendTask masterTask) throws JSONException, SlickException {
        super(name);

        this.masterTask = masterTask;
        this.gameTask = new GenericSendTask();
        this.gameTask.addObserver(this);
        this.inputTask = new GenericSendTask();
        this.inputTask.addObserver(this);
        this.interfaceTask = new GenericSendTask();
        this.interfaceTask.addObserver(this);
        this.accountTask = new GenericSendTask();
        this.accountTask.addObserver(this);
        this.addState(new WindowLogin(EnumWindow.LOGIN.getValue()));
        this.addState(new WindowAccount(EnumWindow.ACCOUNT.getValue(), this.accountTask));
        this.addState(new WindowInterface(EnumWindow.INTERFACE.getValue(), this.gameTask));
        this.addState(new WindowGame(EnumWindow.GAME.getValue(), this.inputTask, this.gameTask));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.enterState(EnumWindow.LOGIN.getValue());
    }

    @Override
    public boolean closeRequested() {
        return false;
    }

    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (!(task.getV1().equals(EnumTargetTask.WINDOWS))) {
            if (task.getV2().isIn(EnumTargetTask.NETWORK)) {
                this.masterTask.sendTask(task);
            } else if (task.getV2().isIn(EnumTargetTask.WINDOWS)) {
                this.doTask(o, task);
            }
        }
    }

    public void doTask(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (task.getV2().equals(EnumTargetTask.GAME)) {
            this.gameTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        } else if (task.getV2().equals(EnumTargetTask.ACCOUNT)) {
            this.accountTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        } else if (task.getV2().equals(EnumTargetTask.INTERFACE)) {
            this.interfaceTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        }
    }
}
