package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.lwjglslick.input.LwjglInputSystem;
import de.lessvoid.nifty.nulldevice.NullInputSystem;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * Created by andres_k on 10/03/2015.
 */
/*
public class WindowLogin extends BasicGameState {
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;
    private TextField fLogin = null;
    private TextField fPassword = null;
    private Image logo = null;
    private Font font = null;


    public WindowLogin(int id) throws JSONException {
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
        this.logo = new Image("assets/img/logo.png");
        this.font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 26), false);
        this.fLogin = new TextField(gameContainer, this.font, 320, 300, 640, 40);
        this.fPassword = new TextField(gameContainer, this.font, 320, 380, 640, 40);
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setBackground(org.newdawn.slick.Color.white);
        this.logo.draw(320, 100, 0.5f);
        this.fLogin.render(gameContainer, g);
        this.fPassword.render(gameContainer, g);
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {

                // TODO REMOVE THIS ON RELEASE
                CurrentUser.setId(UUID.randomUUID().toString());
                CurrentUser.setPseudo(this.fLogin.getText());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());

            } else {
                try {
                    Pair<Boolean, String> p = DataServer.authentification(this.fLogin.getText(), this.fPassword.getText());

                    if (p.getV1()) {

                        JSONObject object = new JSONObject(p.getV2());

                        Debug.debug("my id = " + object.get("_id"));

                        CurrentUser.setId(object.get("_id").toString());
                        CurrentUser.setPseudo(object.get("username").toString());
                        this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
                    } else {

                        // TODO Display message on client.
                        Debug.debug("Error Login : " + p.getV2());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (key == Input.KEY_TAB) {
            if (this.fLogin.hasFocus())
                this.fPassword.setFocus(true);
            else if (this.fPassword.hasFocus()) {
                this.fLogin.setFocus(true);
            }
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
*/


class WindowLogin extends BasicGameState {
    private InputCheck input;
    private int id;

    private Nifty nifty;

    public WindowLogin(int id) throws JSONException {
        this.id = id;
        this.input = new InputCheck(StringTools.readFile("configInput.json"));

    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        try {
            InputSystem input = new LwjglInputSystem();
            ((LwjglInputSystem)input).startup();
            nifty = new Nifty(new LwjglRenderDevice(), new NullSoundDevice(), new NullInputSystem(), new AccurateTimeProvider());

            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
            nifty.fromXml("assets/interface/test.xml", "start");
            nifty.validateXml("assets/interface/test.xml");

            nifty.gotoScreen("start");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}