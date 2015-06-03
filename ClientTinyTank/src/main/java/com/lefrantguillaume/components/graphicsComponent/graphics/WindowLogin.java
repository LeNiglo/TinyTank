package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Browser;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.networkComponent.networkData.DataServer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.net.URI;
import java.net.URISyntaxException;
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

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        this.nifty.update();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
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
        try {
            this.nifty.fromXml("assets/interface/gui-login.xml", "start", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (this.loginField == null) {
            return;
        }
        if (key == Input.KEY_RETURN) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {
                // TODO REMOVE THIS ON RELEASE
                CurrentUser.setId(UUID.randomUUID().toString());
                CurrentUser.setPseudo("CHEAT - "+this.loginField.getDisplayedText());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            } else {
                this.connect();
            }
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.loginField = screen.findNiftyControl("login", TextField.class);
        this.passField = screen.findNiftyControl("pass", TextField.class);
    }

    @Override
    public void onStartScreen() {}

    @Override
    public void onEndScreen() {}

    public void connect() {
        try {
            Pair<Boolean, String> authentication = DataServer.authentification(this.loginField.getDisplayedText(), this.passField.getRealText());
            if (authentication.getV1()) {
                JSONObject object = new JSONObject(authentication.getV2());
                Debug.debug("my id = " + object.get("_id"));
                CurrentUser.setId(object.get("_id").toString());
                CurrentUser.setPseudo(object.get("username").toString());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            } else {
                // TODO Display message on client.
                Debug.debug("Error Login : " + authentication.getV2());
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

}
