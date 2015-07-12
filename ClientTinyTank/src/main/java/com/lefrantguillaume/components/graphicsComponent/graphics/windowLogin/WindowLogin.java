package com.lefrantguillaume.components.graphicsComponent.graphics.windowLogin;

import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.graphics.WindowBasedGame;
import com.lefrantguillaume.components.graphicsComponent.sounds.EnumSound;
import com.lefrantguillaume.components.graphicsComponent.sounds.MusicController;
import com.lefrantguillaume.components.networkComponent.networkData.DataServer;
import com.lefrantguillaume.utils.configs.CurrentUser;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.tools.Browser;
import com.lefrantguillaume.utils.tools.ConsoleWriter;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.StateBasedGame;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowLogin extends WindowBasedGame implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateGame;
    private TextField loginField = null;
    private TextField passField = null;
    private Image background;
    private int id;

    private Nifty nifty;

    public WindowLogin(int id, Nifty nifty) throws JSONException {
        this.id = id;
        this.nifty = nifty;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.background = new Image("assets/old/img/interface/back.png");
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        this.nifty.update();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(this.background, 0, 0);
        SlickCallable.enterSafeBlock();
        this.nifty.render(false);
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

        this.nifty.gotoScreen("screen-login");
        MusicController.loop(EnumSound.BACKGROUND);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.clean();
    }

    @Override
    public void keyReleased(int key, char c) {
        if (this.loginField == null) {
            return;
        }
        if (key == Input.KEY_C) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                // TODO REMOVE THIS ON RELEASE
                CurrentUser.setId(UUID.randomUUID().toString());
                CurrentUser.setPseudo("CHEAT -" + this.loginField.getDisplayedText());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            }
        }
        if (key == Input.KEY_RETURN) {
            this.connect();
        } else if (key == Input.KEY_ESCAPE) {
            this.quit();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.loginField = screen.findNiftyControl("login", TextField.class);
        this.passField = screen.findNiftyControl("pass", TextField.class);
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void connect() {
        try {
            Pair<Boolean, String> authentication = DataServer.authentification(this.loginField.getRealText(), this.passField.getRealText());
            if (authentication.getV1()) {
                JSONObject object = new JSONObject(authentication.getV2());
                ConsoleWriter.debug("my id = " + object.get("_id"));
                CurrentUser.setId(object.get("_id").toString());
                CurrentUser.setPseudo(object.get("username").toString());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            } else {
                // TODO Display message on client.
                ConsoleWriter.debug("Error Login : " + authentication.getV2());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        try {
            Browser.openWebpage(new URI("http://tinytank.lefrantguillaume.com/register"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clean() {
        MusicController.stop(EnumSound.BACKGROUND);
    }

    @Override
    public void quit() {
        this.clean();
        this.container.exit();
    }
}
