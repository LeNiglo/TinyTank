package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.components.gameComponent.controllers.AccountController;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateGame;
    private Nifty nifty;
    private List<ServerEntry> servers = new ArrayList<>();
    private int current = 0;
    private AccountController accountController;

    private int id;


    public WindowAccount(int id, Nifty nifty, GenericSendTask accountTask) throws JSONException {
        this.id = id;
        this.nifty = nifty;
        this.accountController = new AccountController();
        this.accountController.addObserver(accountTask);
        accountTask.addObserver(this.accountController);
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.accountController.setStateGame(stateBasedGame);
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        try {
            nifty.fromXml("assets/interface/gui-account.xml", "main", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.current = 0;
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
        this.servers = this.accountController.createServerList();
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        SlickCallable.enterSafeBlock();
        this.nifty.render(false);
        SlickCallable.leaveSafeBlock();

        g.setColor(Color.green);

        for (int i = 0; i < this.servers.size(); i++) {
            g.drawString(servers.get(i).toString(), 20, 20 * (i + 1));
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        this.nifty.update();
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
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                // TODO REMOVE THIS ON RELEASE

                ServerEntry srv = new ServerEntry("Server de Hacker", "127.0.0.1", null);
                srv.setTcpPort(13333);
                srv.setUdpPort(13444);
                this.accountController.connect(srv);

            } else {

                ServerEntry srv = null;
                if (this.current < this.servers.size()) {
                    srv = this.servers.get(this.current);
                }
                this.accountController.connect(srv);
            }
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        } else if (key == Input.KEY_SPACE) {
            this.servers = this.accountController.createServerList();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}
