package com.lefrantguillaume.components.graphicsComponent.graphics.windowAccount;

import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.gameComponent.controllers.AccountController;
import com.lefrantguillaume.components.graphicsComponent.graphics.WindowBasedGame;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.input.InputData;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.windowOverlay.AccountOverlay;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.Overlay;
import com.lefrantguillaume.components.networkComponent.ServerEntry;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.utils.tools.ConsoleWriter;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends WindowBasedGame implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateWindow;
    private AccountController accountController;
    private final ScheduledExecutorService scheduler;

    private Image background;

    private Nifty nifty;
    private int id;
    private ListBox listBox = null;
    private Future<?> future = null;


    private AnimatorOverlayData animatorOverlay;
    private Overlay accountOverlay;

    public WindowAccount(int id, Nifty nifty, GenericSendTask accountTask) throws JSONException {
        this.id = id;
        this.nifty = nifty;

        this.scheduler = Executors.newScheduledThreadPool(1);

        this.accountController = new AccountController();
        this.accountController.addObserver(accountTask);
        accountTask.addObserver(this.accountController);

        this.animatorOverlay = new AnimatorOverlayData();
        InputData inputData = new InputData("configInput.json");
        this.accountOverlay = new AccountOverlay(inputData);
        accountTask.addObserver(this.accountOverlay);
        this.accountOverlay.addObserver(accountTask);
        ConsoleWriter.debug("end constructor Interface");
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateWindow = stateBasedGame;
        this.container.setForceExit(false);
        this.accountController.setStateWindow(this.stateWindow);
        this.accountController.setWindow(this);

        this.animatorOverlay.init();
        this.accountOverlay.initElementsComponent(this.animatorOverlay);
        this.background = new Image("assets/old/img/interface/log_back.png");
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
        this.accountController.clearServers();

        this.nifty.gotoScreen("screen-account");


        this.future = this.scheduler.scheduleWithFixedDelay(() -> {
            WindowAccount.this.accountController.createServerList();
            WindowAccount.this.fillServerList();
        }, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.clean();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.drawImage(this.background, 0, 0);
        SlickCallable.enterSafeBlock();
        this.nifty.render(false);
        SlickCallable.leaveSafeBlock();
        this.accountOverlay.draw(g);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        this.nifty.update();
        this.accountOverlay.updateOverlay();
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (!this.accountOverlay.isOnFocus(x, y)) {
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        this.accountOverlay.event(key, c, EnumInput.PRESSED);
    }

    @Override
    public void keyReleased(int key, char c) {
        // TODO REMOVE THIS ON RELEASE
        boolean absorbed = this.accountOverlay.event(key, c, EnumInput.RELEASED);
        if (absorbed == false) {
            if (key == Input.KEY_C) {
                if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                    ConsoleWriter.write("Connected as hacker");
                    ServerEntry srv = new ServerEntry("Server de Hacker", "37.187.0.176", "Cheated Map", 13333, 13444);
                    ConsoleWriter.debug("Trying to cheat ! Connect to " + srv);
                    this.accountController.connect(srv);
                }
            } else if (key == Input.KEY_ENTER) {

            } else if (key == Input.KEY_R) {
                if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                    this.accountController.createServerList();
                    this.fillServerList();
                }
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

    @Override
    public void clean() {
        this.future.cancel(true);
        this.accountController.clearServers();
        this.accountOverlay.leave();
    }

    @Override
    public void quit() {
        this.clean();
        this.scheduler.shutdown();
        this.container.exit();
    }
}
