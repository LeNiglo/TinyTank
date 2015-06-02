package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.networkComponent.networkData.DataServer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.UUID;


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowLogin extends BasicGameState implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateGame;
    private TextField loginField = null;
    private TextField passField = null;
    private int id;

    private Nifty nifty;

    public WindowLogin(int id, Nifty nifty) throws JSONException {
        this.id = id;
        this.nifty = nifty;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);


    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        nifty.update();
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        SlickCallable.enterSafeBlock();
        nifty.render(false);
        SlickCallable.leaveSafeBlock();
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        try {


            nifty.fromXml("assets/interface/gui-login.xml", "start", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (loginField == null) {
            return;
        }
        if (key == Input.KEY_RETURN) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                // TODO REMOVE THIS ON RELEASE
                CurrentUser.setId(UUID.randomUUID().toString());
                CurrentUser.setPseudo(loginField.getDisplayedText());
                System.out.println(loginField.getDisplayedText());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            } else {
                System.out.println("Press ctrl + enter pls");
            }
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // on est bind dans le xml. le screen id="start" est relié à ce controlleur
        System.out.println("LoginController binded");
        loginField = screen.findNiftyControl("login", TextField.class);
        passField = screen.findNiftyControl("pass", TextField.class);
    }

    @Override
    public void onStartScreen() {
        System.out.println("LoginController started");
    }

    @Override
    public void onEndScreen() {
        System.out.println("LoginController ended");
    }

    public void connect() {
        System.out.println("Cliqué sur Connect !");
        try {
            System.out.println("user: " + loginField.getDisplayedText() + " // pass: " + passField.getRealText());
            Pair<Boolean, String> p = DataServer.authentification(loginField.getDisplayedText(), passField.getRealText());
            if (p.getV1()) {
                JSONObject object = new JSONObject(p.getV2());
                Debug.debug("my id = " + object.get("_id"));
                CurrentUser.setId(object.get("_id").toString());
                CurrentUser.setPseudo(object.get("username").toString());
                stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            } else {
                // TODO Display message on client.
                Debug.debug("Error Login : " + p.getV2());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}