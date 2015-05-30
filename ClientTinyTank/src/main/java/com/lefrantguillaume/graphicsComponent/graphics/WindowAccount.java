package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import com.lefrantguillaume.networkComponent.ServerEntry;
import com.lefrantguillaume.networkComponent.networkData.DataServer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState {
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private Image background = null;
    private List<ServerEntry> servers = new ArrayList<>();
    private int id;

    public WindowAccount(int id) throws JSONException {
        this.id = id;
        this.input = new InputCheck(StringTools.readFile("configInput.json"));
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.background = new Image("assets/img/ex_acc.png");
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException{
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        try {
            Pair<Boolean, String> p = DataServer.getServerList();

            if (p.getV1()) {

                JSONArray array = new JSONArray(p.getV2());

                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);

                    try {
                        Debug.debug(p.getV2());
                        servers.add(new ServerEntry(row.getString("name"), row.getString("ip"), row.getJSONObject("ports"), row.getJSONArray("users"), row.getString("map"), row.getString("started_at"), row.getString("last_active")));
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

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.drawImage(this.background, 0, 0);
        g.setColor(Color.green);
        for (int i = 0; i < this.servers.size(); i++) {
            g.drawString(this.servers.get(i).toString(), 20, 20 * (i + 1));
        }
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
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

            this.stateGame.enterState(EnumWindow.INTERFACE.getValue());
        }
        else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
