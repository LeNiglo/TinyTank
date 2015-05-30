package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.networkComponent.networkData.DataServer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
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

class WindowLogin extends BasicGameState {
    private GameContainer container;
    private StateBasedGame stateGame;
    private LwjglInputSystem inputSys;
    private int id;

    private Nifty nifty;

    public WindowLogin(int id) throws JSONException {
        this.id = id;
        //this.input = new InputCheck(StringTools.readFile("configInput.json"));
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        try {
            SlickInputSystem inputSys = new PlainSlickInputSystem();
            nifty = new Nifty(new LwjglRenderDevice(), new NullSoundDevice(), inputSys, new AccurateTimeProvider());

            gameContainer.getInput().removeListener(this);
            gameContainer.getInput().removeListener(inputSys);
            gameContainer.getInput().addListener(inputSys);

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

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
    }

    @Override
    public void keyPressed(int key, char c) {
        System.out.println("key pressed:" + c);
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {

                // TODO REMOVE THIS ON RELEASE
                CurrentUser.setId(UUID.randomUUID().toString());
                CurrentUser.setPseudo("login"); //TODO remplacer login par la vraie value du loginText
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());

            } else {
                try {
                    Pair<Boolean, String> p = DataServer.authentification("user", "pass"); // TODO voir le TODO d'au dessus

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
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

    public void connect() {
        System.out.println("Cliqué sur Connect !");
    }
}