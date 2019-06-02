package com.lefrantguillaume.components.gameComponent.controllers;

import com.esotericsoftware.kryonet.Server;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.graphics.WindowBasedGame;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.networkComponent.networkData.DataServer;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import com.lefrantguillaume.utils.configs.NetworkServerConfig;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.utils.tools.ConsoleWriter;
import com.lefrantguillaume.utils.tools.FastHack;
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
    private StateBasedGame stateWindow;
    private WindowBasedGame window;

    private List<ServerEntry> servers = new ArrayList<>();

    public AccountController() {
        this.window = null;
        this.stateWindow = null;
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().equals(EnumTargetTask.ACCOUNT)) {
            ConsoleWriter.debug("accountTask: " + received);
            if (received.getV3() instanceof EnumWindow) {
                if (received.getV3() == EnumWindow.EXIT && this.window != null) {
                    this.window.quit();
                } else if (this.stateWindow != null) {
                    this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
                }
            }
        }
    }
/*
    public List<ServerEntry> createServerList() {
        this.servers.clear();
        ServerEntry server = new ServerEntry(FastHack.name, FastHack.ip, FastHack.map, FastHack.tcp, FastHack.udp);
        this.servers.add(server);
        return this.servers;
    }*/

    public List<ServerEntry> createServerList() {
        try {
            Pair<Boolean, String> p = DataServer.getServerList();

            if (p.getV1()) {
                JSONArray array = new JSONArray(p.getV2());
                this.servers.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    try {
                        ConsoleWriter.debug(p.getV2());
                        this.servers.add(new ServerEntry(row.getString("name"), row.getString("ip"), row.getJSONObject("ports"), row.getJSONArray("users"), row.getString("map"), row.getString("started_at"), row.getString("last_active")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return this.servers;
            } else {
                // TODO Display message on client.
                ConsoleWriter.debug("Error Login : " + p.getV2());
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

    public void setStateWindow(StateBasedGame stateWindow) {
        this.stateWindow = stateWindow;
    }

    public void setWindow(WindowBasedGame window) {
        this.window = window;
    }

    public void clearServers() {
        this.servers.clear();
    }

    // GETTERS

    public List<ServerEntry> getServers() {
        return this.servers;
    }
}
