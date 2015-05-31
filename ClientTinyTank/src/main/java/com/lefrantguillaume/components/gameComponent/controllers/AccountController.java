package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.NetworkServerConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.networkComponent.networkData.DataServer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 31/05/2015.
 */
public class AccountController extends Observable implements Observer {
    private List<ServerEntry> servers;
    private Image background = null;
    private int current;
    private StateBasedGame stateWindow;

    public AccountController() {
        this.servers = new ArrayList<>();
        this.current = 0;
        this.stateWindow = null;
    }

    //FUNCTIONS

    public void init() throws SlickException {
        this.background = new Image("assets/img/ex_acc.png");
    }

    public void render(Graphics g) {
        g.drawImage(this.background, 0, 0);
        g.setColor(Color.green);
        for (int i = 0; i < this.servers.size(); i++) {
            g.drawString(this.servers.get(i).toString(), 20, 20 * (i + 1));
        }
    }

    public void createServerList() {
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
            } else {
                // TODO Display message on client.
                Debug.debug("Error Login : " + p.getV2());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() {
        ServerEntry server = null;
        if (this.current < this.servers.size()) {
            server = this.servers.get(this.current);
        }
        if (server != null) {
            NetworkServerConfig request = new NetworkServerConfig(server.getUdpPort(), server.getTcpPort(), server.getIp());
            this.setChanged();
            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.ACCOUNT, EnumTargetTask.CONFIG_SERVER, request));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV2().equals(EnumTargetTask.ACCOUNT)) {
            Debug.debug("accountTask: " + received);
            if (received.getV3() instanceof EnumWindow && this.stateWindow != null) {
                this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
            }
        }
    }

    //SETTERS
    public void setStateWindow(StateBasedGame stateWindow) {
        this.stateWindow = stateWindow;
    }
}
