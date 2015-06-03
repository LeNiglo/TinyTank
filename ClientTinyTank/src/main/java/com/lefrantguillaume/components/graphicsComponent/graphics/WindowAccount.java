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
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateGame;
    private AccountController accountController;
    private Nifty nifty;
    private int current = 0;
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
        try {
            nifty.fromXml("assets/interface/gui-account.xml", "screen-account", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.current = 0;
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
        this.accountController.createServerList();
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

        for (int i = 0; i < this.accountController.servers.size(); i++) {
            g.drawString(this.accountController.servers.get(i).toString(), 20, 20 * (i + 1));
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

                this.accountController.connect(new ServerEntry("Server de Hacker", "127.0.0.1", 13333, 13444));

            } else {

                ServerEntry srv = null;
                if (this.current < this.accountController.servers.size()) {
                    srv = this.accountController.servers.get(this.current);
                }
                this.accountController.connect(srv);
            }
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        } else if (key == Input.KEY_SPACE) {
            this.accountController.createServerList();
            this.fillServerList();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        Debug.debug("BIND ACCOUNT ?");
        this.listBox = screen.findNiftyControl("list-servers", ListBox.class);
        Debug.debug("BIND LIST BOX ?");
        this.fillServerList();
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void fillServerList() {
        if (this.listBox != null) {
            this.listBox.clear();
            for (int i = 0; i < this.accountController.servers.size(); i++) {
                this.listBox.addItem(this.accountController.servers.get(i));
            }
        }
    }


    public void onServerEntrySelected(final String id, final ListBoxSelectionChangedEvent<String> event) {
        Debug.debug("Clicked ... ON WHAT ?!");
        List<String> selection = event.getSelection();
        for (String selectedItem : selection) {
            System.out.println("listServers selection [" + selectedItem.toString() + "]");
        }
    }

    /**
     * When the selection of the ListBox changes this method is called.
     */
    @NiftyEventSubscriber(id = "list-servers")
    public void onListServerSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        List<String> selection = event.getSelection();
        for (String selectedItem : selection) {
            System.out.println("listServers selection [" + selectedItem + "]");
        }
    }


}
