package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.gameComponent.controllers.InterfaceController;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglBatchRenderBackendFactory;
import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickSlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 17/03/2015.
 */
public class Windows extends NiftyStateBasedGame {

    private final String name;
    private final List<Observer> observers;
    private final InterfaceController interfaceController;
    private final GameController gameController;
    private Nifty nifty = null;

    public Windows(String name, List<Observer> observers, InterfaceController interfaceController, GameController gameController) throws JSONException {
        super(name);


        this.name = name;
        this.observers = observers;
        this.interfaceController = interfaceController;
        this.gameController = gameController;
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        SlickInputSystem inputSystem = new PlainSlickInputSystem();
        Input input = this.getContainer().getInput();

        inputSystem.setInput(input);
        input.addListener(inputSystem);

        nifty = new Nifty(new BatchRenderDevice(LwjglBatchRenderBackendFactory.create()), new NullSoundDevice(), inputSystem, new AccurateTimeProvider());


        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");


        try {
            this.addState(new WindowLogin(EnumWindow.LOGIN.getValue(), nifty));
            this.addState(new WindowAccount(EnumWindow.ACCOUNT.getValue(), nifty));
            this.addState(new WindowInterface(EnumWindow.INTERFACE.getValue(), nifty, observers, interfaceController));
            this.addState(new WindowGame(EnumWindow.GAME.getValue(), nifty, observers, gameController));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.enterState(EnumWindow.LOGIN.getValue());
    }
}
