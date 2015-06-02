package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.NetworkServerConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.graphicsComponent.input.InputGame;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.networkComponent.networkData.DataServer;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState implements ScreenController, Observer {
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputGame input;
    private List<ServerEntry> servers = new ArrayList<>();
    private int current = 0;

    private int id;
    private Nifty nifty;
    private GenericSendTask accountTask;


    public WindowAccount(int id, Nifty nifty, GenericSendTask accountTask) throws JSONException {
        this.id = id;
        this.nifty = nifty;
        this.accountTask = accountTask;
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);

    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        try {

            nifty.fromXml("assets/interface/gui-account.xml", "main", this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
        this.createServerList();
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        // g.drawImage(this.background, 0, 0);
        SlickCallable.enterSafeBlock();
        nifty.render(false);
        SlickCallable.leaveSafeBlock();

        g.setColor(Color.green);
        for (int i = 0; i < this.servers.size(); i++) {
            g.drawString(this.servers.get(i).toString(), 20, 20 * (i + 1));
        }
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        nifty.update();
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            this.connect();
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        } else if (key == Input.KEY_SPACE) {
            this.createServerList();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        Debug.debug("SCREEN BIND");
    }

    @Override
    public void onStartScreen() {
        Debug.debug("SCREEN START");
    }

    @Override
    public void onEndScreen() {
        Debug.debug("SCREEN END");
    }

    private void createServerList() {
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

    public void connect() {

        ServerEntry server = null;
        if (this.current < this.servers.size()) {
            server = this.servers.get(this.current);
        }
        if (server != null) {
            NetworkServerConfig request = new NetworkServerConfig(server.getUdpPort(), server.getTcpPort(), server.getIp());

            this.accountTask.sendTask(TaskFactory.createTask(EnumTargetTask.ACCOUNT, EnumTargetTask.CONFIG_SERVER, request));

        }

        this.stateGame.enterState(EnumWindow.INTERFACE.getValue());

    }

    @Override
    public void update(Observable observable, Object o) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) o;

        if (received.getV2().equals(EnumTargetTask.ACCOUNT)) {
            Debug.debug("accountTask: " + received);
            if (received.getV3() instanceof EnumWindow && this.stateGame != null) {
                this.stateGame.enterState(((EnumWindow) received.getV3()).getValue());
            }
        }
    }
}
