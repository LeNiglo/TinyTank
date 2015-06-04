package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.stockage.Tuple;
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

    private WindowLogin wl;
    private WindowAccount wa;
    private WindowInterface wi;
    private WindowGame wg;

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

        this.addState(this.wl);
        this.addState(this.wa);
        this.addState(this.wi);
        this.addState(this.wg);

        if (!this.initNifty(false)) {
            throw new SlickException("Wrong Custom Init end.");
        }

        this.enterState(EnumWindow.LOGIN.getValue());
    }

    private boolean initScreenControllers() {
        try {
            this.wl = new WindowLogin(EnumWindow.LOGIN.getValue(), this.nifty);
            this.wa = new WindowAccount(EnumWindow.ACCOUNT.getValue(), this.nifty, this.accountTask);
            this.wi = new WindowInterface(EnumWindow.INTERFACE.getValue(), this.nifty, this.gameTask);
            this.wg = new WindowGame(EnumWindow.GAME.getValue(), this.nifty, this.inputTask, this.gameTask);
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
            this.nifty.registerScreenController(this.wl);
            this.nifty.registerScreenController(this.wa);
            this.nifty.registerScreenController(this.wi);
            this.nifty.registerScreenController(this.wg);
            this.nifty.addXml("assets/interface/gui-login.xml");
            this.nifty.addXml("assets/interface/gui-account.xml");
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

        if (task.getV2().equals(EnumTargetTask.GAME)) {
            this.gameTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        } else if (task.getV2().equals(EnumTargetTask.ACCOUNT)) {
            this.accountTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        } else if (task.getV2().equals(EnumTargetTask.INTERFACE)) {
            this.interfaceTask.sendTask(TaskFactory.createTask(EnumTargetTask.WINDOWS, task));
        }
    }
}
