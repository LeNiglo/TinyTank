package com.lefrantguillaume.gestGame.master;

import com.lefrantguillaume.gestGame.Utils.configs.MasterConfig;
import com.lefrantguillaume.gestGame.Utils.configs.NetworkServerConfig;
import com.lefrantguillaume.gestGame.gameComponent.controllers.InterfaceController;
import com.lefrantguillaume.gestGame.gameComponent.controllers.GameController;
import com.lefrantguillaume.gestGame.graphicsComponent.graphics.Windows;
import com.lefrantguillaume.gestGame.Utils.configs.WindowConfig;
import com.lefrantguillaume.gestGame.networkComponent.NetworkCall;
import com.lefrantguillaume.gestGame.networkComponent.NetworkMessage;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class MasterGame {
    private GameController gameController;
    private InterfaceController interfaceController;
    private NetworkCall networkCall;
    private NetworkMessage masterRequestQueue;
    private NetworkMessage masterResponseQueue;
    private GenericRequestObserver genericRequestObserver;
    private MasterRequestController masterRequestController;
    private MasterResponseController masterResponseController;

    public MasterGame() throws SlickException {
        this.initGame();
        this.initNetwork();
        this.initInput();
    }

    private void initGame() throws SlickException {
        this.gameController = new GameController();
        this.interfaceController = new InterfaceController();
    }

    private void initNetwork() {
        this.masterRequestQueue = new NetworkMessage();
        this.masterResponseQueue = new NetworkMessage();
        this.masterResponseController = new MasterResponseController(this.masterResponseQueue);
        this.networkCall = new NetworkCall(new NetworkServerConfig(MasterConfig.getMasterUdpPort(), MasterConfig.getMasterTcpPort(), MasterConfig.getMasterAddress()));
        this.networkCall.addObserver(this.masterResponseController);
        this.masterRequestController = new MasterRequestController(this.masterRequestQueue, this.networkCall);
        this.masterRequestQueue.addObserver(this.masterRequestController);
    }

    private void initInput() {
        this.genericRequestObserver = new GenericRequestObserver(this.masterRequestQueue);
    }

    public void start() {
        try {
            this.startGame();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private void startGame() throws SlickException {
        List<Observer> observers = new ArrayList<Observer>();
        observers.add(this.genericRequestObserver);
        this.masterResponseController.addObserver(this.interfaceController);
        this.masterResponseController.addObserver(this.gameController);
        AppGameContainer appGame = new AppGameContainer(new Windows("TinyTank", observers, this.interfaceController, this.gameController));
        appGame.setDisplayMode(WindowConfig.getSizeX(), WindowConfig.getSizeY(), false);
        appGame.start();

    }
}
