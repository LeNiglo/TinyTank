package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.controllers.AccountController;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateGame;
    private AccountController accountController;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Nifty nifty;
    private int id;
    private ListBox listBox = null;

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
        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        this.nifty.gotoScreen("screen-account");

        this.scheduler.scheduleAtFixedRate(() -> {
            WindowAccount.this.accountController.createServerList();
            WindowAccount.this.fillServerList();
        }, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        SlickCallable.enterSafeBlock();
        this.nifty.render(false);
        SlickCallable.leaveSafeBlock();
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
        // TODO REMOVE THIS ON RELEASE
        if (key == Input.KEY_C) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                ServerEntry srv = new ServerEntry("Server de Hacker", "127.0.0.1", "Cheated Map", 13333, 13444);
                Debug.debug("Trying to cheat ! Connect to " + srv);
                this.accountController.connect(srv);
            }
        } else if (key == Input.KEY_ENTER) {

        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
            this.scheduler.shutdown();
        } else if (key == Input.KEY_R) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                this.accountController.createServerList();
                this.fillServerList();
            }
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.listBox = screen.findNiftyControl("list-servers", ListBox.class);
        this.fillServerList();
    }

    private void fillServerList() {
        if (this.listBox != null) {
            this.listBox.clear();
            for (int i = 0; i < this.accountController.getServers().size(); i++) {
                this.listBox.addItem(this.accountController.getServers().get(i));
            }
        }
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    @NiftyEventSubscriber(id = "list-servers")
    public void onListServersSelectionChanged(final String id, final ListBoxSelectionChangedEvent<ServerEntry> event) {
        List<Integer> indices = event.getSelectionIndices();
        for (Integer i : indices) {
            if (i < this.accountController.getServers().size()) {
                this.accountController.connect(this.accountController.getServers().get(i));
            }
        }
    }
}
