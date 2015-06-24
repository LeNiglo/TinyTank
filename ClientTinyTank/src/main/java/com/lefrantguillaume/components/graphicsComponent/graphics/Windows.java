package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglBatchRenderBackendFactory;
import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 17/03/2015.
 */
public class Windows extends NiftyStateBasedGame implements Observer {

    private GenericSendTask gameTask;
    private GenericSendTask inputTask;
    private GenericSendTask interfaceTask;
    private GenericSendTask accountTask;
    private GenericSendTask masterTask;

    private WindowLogin windowLogin;
    private WindowAccount windowAccount;
    private WindowInterface windowInterface;
    private WindowGame windowGame;

    private Nifty nifty = null;

    public Windows(String name, GenericSendTask masterTask) throws JSONException, SlickException {
        super(name);
        this.masterTask = masterTask;
        this.gameTask = new GenericSendTask();
        this.gameTask.addObserver(this);
        this.inputTask = new GenericSendTask();
        this.inputTask.addObserver(this);
        this.interfaceTask = new GenericSendTask();
        this.interfaceTask.addObserver(this);
        this.accountTask = new GenericSendTask();
        this.accountTask.addObserver(this);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        if (!this.initNifty(true) || !this.initScreenControllers()) {
            throw new SlickException("Wrong Custom Init begin.");
        }

        this.addState(this.windowLogin);
        this.addState(this.windowAccount);
        this.addState(this.windowInterface);
        this.addState(this.windowGame);

        if (!this.initNifty(false)) {
            throw new SlickException("Wrong Custom Init end.");
        }

        this.enterState(EnumWindow.LOGIN.getValue());
    }

    private boolean initScreenControllers() {
        try {
            this.windowLogin = new WindowLogin(EnumWindow.LOGIN.getValue(), this.nifty);
            this.windowAccount = new WindowAccount(EnumWindow.ACCOUNT.getValue(), this.nifty, this.accountTask);
            this.windowInterface = new WindowInterface(EnumWindow.INTERFACE.getValue(), this.nifty, this.gameTask);
            this.windowGame = new WindowGame(EnumWindow.GAME.getValue(), this.nifty, this.inputTask, this.gameTask);
            return true;
        } catch (SlickException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeRequested() {
        return false;
    }

    public boolean initNifty(boolean start) {
        if (start) {
            SlickInputSystem inputSystem = new PlainSlickInputSystem();
            Input input = this.getContainer().getInput();

            inputSystem.setInput(input);
            input.addListener(inputSystem);
            this.nifty = new Nifty(new BatchRenderDevice(LwjglBatchRenderBackendFactory.create()), new NullSoundDevice(), inputSystem, new AccurateTimeProvider());
            this.nifty.loadStyleFile("nifty-default-styles.xml");
            this.nifty.loadControlFile("nifty-default-controls.xml");
            return true;
        } else {
            this.nifty.registerScreenController(this.windowLogin);
            this.nifty.registerScreenController(this.windowAccount);
            this.nifty.registerScreenController(this.windowInterface);
            this.nifty.registerScreenController(this.windowGame);
            this.nifty.addXml("assets/old/interface/gui-login.xml");
            this.nifty.addXml("assets/old/interface/gui-account.xml");
            this.nifty.addXml("assets/old/interface/gui-interface.xml");
            this.nifty.addXml("assets/old/interface/gui-game.xml");
            this.nifty.registerMouseCursor("crosshair", "assets/old/img/game/cursor.png", 14, 15);
            // this.nifty.setDebugOptionPanelColors(true);
            return true;
        }
    }

    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (!(task.getV1().equals(EnumTargetTask.WINDOWS))) {
            if (task.getV2().isIn(EnumTargetTask.NETWORK)) {
                this.masterTask.sendTask(task);
            } else if (task.getV2().isIn(EnumTargetTask.WINDOWS)) {
                this.doTask(o, task);
            }
        }
    }

    public void doTask(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (task.getV2().isIn(EnumTargetTask.GAME)) {
            this.redirectGame(task);
            this.gameTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        } else if (task.getV2().isIn(EnumTargetTask.ACCOUNT)) {
            this.accountTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        } else if (task.getV2().isIn(EnumTargetTask.INTERFACE)) {
            this.interfaceTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        }
    }

    private void redirectGame(Tuple<EnumTargetTask, EnumTargetTask, Object> task){
        if (task.getV3() instanceof MessageChat){
            task.setV2(EnumTargetTask.GAME_OVERLAY);
        }
    }
}
