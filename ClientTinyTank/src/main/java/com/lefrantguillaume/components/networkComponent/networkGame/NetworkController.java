package com.lefrantguillaume.components.networkComponent.networkGame;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lefrantguillaume.utils.configs.NetworkServerConfig;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkController {
    private Client client;
    private GenericSendTask masterTask;

    public NetworkController(GenericSendTask masterTask) {
        this.masterTask = masterTask;
        this.client = new Client();
        NetworkRegister.register(this.client);
        this.client.start();
    }

    //FUNCTIONS
    public boolean connect(NetworkServerConfig config) {
        try {
            this.client.connect(5000, config.getAddress(), config.getTcpPort(), config.getUdpPort());
            this.client.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof MessageModel) {
                        MessageModel response = (MessageModel) object;
                        masterTask.sendTask(TaskFactory.createTask(EnumTargetTask.MESSAGE_SERVER, EnumTargetTask.GAME, response));
                    }
                }
            });
            this.masterTask.sendTask(TaskFactory.createTask(EnumTargetTask.MESSAGE_SERVER, EnumTargetTask.ACCOUNT, EnumWindow.INTERFACE));
            return true;
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        this.client.close();
    }

    public void call(MessageModel request) {
        if (this.client.isConnected()) {
            client.sendTCP(request);
        }
    }

    public void doTask(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (task.getV2().equals(EnumTargetTask.MESSAGE_SERVER)) {
            if (task.getV3() instanceof MessageModel) {
                this.call((MessageModel) task.getV3());
            }
        } else if (task.getV2().equals(EnumTargetTask.CONFIG_SERVER)) {
            if (task.getV3() instanceof NetworkServerConfig) {
                this.connect((NetworkServerConfig) task.getV3());
            }
        }
    }
}
