package com.lefrantguillaume.master;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.graphicsComponent.graphics.Windows;
import com.lefrantguillaume.components.networkComponent.networkGame.NetworkController;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class MasterGame implements Observer {
    private NetworkController networkController;
    private GenericSendTask masterTask;
    private Windows windows;

    public MasterGame() throws SlickException, JSONException {
        this.masterTask = new GenericSendTask();
        this.masterTask.addObserver(this);
        this.windows = new Windows("TinyTank Game", this.masterTask);
        this.networkController = new NetworkController(this.masterTask);
    }

    public void start() {
        try {
            this.startGame();
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startGame() throws SlickException, JSONException {
        AppGameContainer appGame = new AppGameContainer(this.windows);
        appGame.setDisplayMode(WindowConfig.getIntSizeX(), WindowConfig.getIntSizeY(), false);
        appGame.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        //Debug.debug("masterTask " + task);
        if (task.getV2().isIn(EnumTargetTask.WINDOWS)){
            this.windows.doTask(o, task);
        } else if (task.getV2().isIn(EnumTargetTask.NETWORK)) {
            this.networkController.doTask(o, task);
        }
    }
}
