package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.NetworkServerConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.networkComponent.networkData.DataServer;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 03/06/2015.
 */
public class AccountController extends Observable implements Observer {
    private StateBasedGame stateGame;
    private List<ServerEntry> servers = new ArrayList<>();

    public AccountController() {
        this.stateGame = null;
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV2().equals(EnumTargetTask.ACCOUNT)) {
            Debug.debug("accountTask: " + received);
            if (received.getV3() instanceof EnumWindow && this.stateGame != null) {
                this.stateGame.enterState(((EnumWindow) received.getV3()).getValue());
            }
        }
    }

    public List<ServerEntry> createServerList() {
        try {
            Pair<Boolean, String> p = DataServer.getServerList();

            if (p.getV1()) {
                JSONArray array = new JSONArray(p.getV2());
                this.servers.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    try {
                        Debug.debug(p.getV2());
                        this.servers.add(new ServerEntry(row.getString("name"), row.getString("ip"), row.getJSONObject("ports"), row.getJSONArray("users"), row.getString("map"), row.getString("started_at"), row.getString("last_active")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return this.servers;
            } else {
                // TODO Display message on client.
                Debug.debug("Error Login : " + p.getV2());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void connect(ServerEntry server) {
        if (server != null) {
            NetworkServerConfig request = new NetworkServerConfig(server.getUdpPort(), server.getTcpPort(), server.getIp());
            this.setChanged();
            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.ACCOUNT, EnumTargetTask.CONFIG_SERVER, request));
        }
    }

    // SETTERS

    public void setStateGame(StateBasedGame stateGame) {
        this.stateGame = stateGame;
    }

    // GETTERS

    public List<ServerEntry> getServers() { return this.servers; }
}
