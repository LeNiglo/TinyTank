package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.*;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ButtonElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ImageElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.*;

/**
 * Created by andres_k on 20/06/2015.
 */
public class GameOverlay extends Observable implements Observer {
    private int current;

    private HashMap<EnumOverlayElement, InterfaceElement> elements;
    private AnimatorOverlayData animatorOverlayData;
    private GenericSendTask genericSendTask;

    public GameOverlay() {
        this.current = 0;

        this.genericSendTask = new GenericSendTask();
        this.genericSendTask.addObserver(this);

        this.elements = new HashMap<>();
        this.elements.put(EnumOverlayElement.CHAT, new ChatElement(EnumOverlayElement.CHAT,
                new BodyRect(new Rectangle(0, WindowConfig.getSizeY() - 200, 400, 200), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
        this.elements.put(EnumOverlayElement.POP_KILL, new StringPopElement(EnumOverlayElement.POP_KILL,
                new BodyRect(new Rectangle(WindowConfig.getSizeX() - 250, 0, 250, 400))));
        this.elements.put(EnumOverlayElement.TABLE_NEW_ROUND, new TableNewRoundElement(EnumOverlayElement.TABLE_NEW_ROUND,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 368, (WindowConfig.getSizeY() / 2) - 72, 700, 300))));
        this.elements.put(EnumOverlayElement.TABLE_STAT, new TableStatElement(EnumOverlayElement.TABLE_STAT,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 400, (WindowConfig.getSizeY() / 2) - 300, 700, 300), new Color(0.1f, 0.2f, 0.3f, 0.5f))));
        this.elements.put(EnumOverlayElement.TABLE_ICON, new TableElement(EnumOverlayElement.TABLE_ICON,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - (192 / 2), WindowConfig.getSizeY() - 64, 192, 64), new Color(0.1f, 0.2f, 0.3f, 0.5f)), true, new boolean[]{true, true}));
        this.elements.put(EnumOverlayElement.CUSTOM_MENU_SCREEN, new CustomElement(EnumOverlayElement.CUSTOM_MENU_SCREEN, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), new Color(0.1f, 0.2f, 0.3f, 0.5f)), false, new boolean[]{true, true}));
        this.elements.put(EnumOverlayElement.CUSTOM_MENU_CONTROLS, new CustomElement(EnumOverlayElement.CUSTOM_MENU_CONTROLS, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), new Color(0.1f, 0.2f, 0.3f, 0.5f)), false, new boolean[]{true, true}));
        this.elements.put(EnumOverlayElement.CUSTOM_MENU_SETTINGS, new CustomElement(EnumOverlayElement.CUSTOM_MENU_SETTINGS, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), new Color(0.1f, 0.2f, 0.3f, 0.5f)), false, new boolean[]{true, true}));
        this.elements.put(EnumOverlayElement.CUSTOM_MENU, new CustomElement(EnumOverlayElement.CUSTOM_MENU, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), new Color(0.1f, 0.2f, 0.3f, 0.5f)), false, new boolean[]{true, true}));
    }

    public void init(AnimatorOverlayData animatorOverlayData) {
        this.animatorOverlayData = animatorOverlayData;

        this.initTableNewRound();
        this.initTableMenu();
        this.initTableMenuScreen();
        this.initTableMenuControls();
        this.initTableMenuSettings();
    }

    private void initTableNewRound() {
        InterfaceElement tableNewRound = this.elements.get(EnumOverlayElement.TABLE_NEW_ROUND);
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.NEW_ROUND), "newRound", Element.PositionInBody.MIDDLE_UP));
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.STATE), "newRound", Element.PositionInBody.MIDDLE_MID));
    }

    private void initTableMenu() {
        InterfaceElement tableMenu = this.elements.get(EnumOverlayElement.CUSTOM_MENU);
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 20, tableMenu.getBody().getSizeX() - 40, 60), new Color(0.1f, 0.2f, 0.3f, 0.5f)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.SCREEN), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.CUSTOM_MENU_SCREEN));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 90, tableMenu.getBody().getSizeX() - 40, 60), new Color(0.1f, 0.2f, 0.3f, 0.5f)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.CONTROLS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.CUSTOM_MENU_CONTROLS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 160, tableMenu.getBody().getSizeX() - 40, 60), new Color(0.1f, 0.2f, 0.3f, 0.5f)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.SETTINGS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.CUSTOM_MENU_SETTINGS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 230, tableMenu.getBody().getSizeX() - 40, 60), new Color(0.1f, 0.2f, 0.3f, 0.5f)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.EXIT), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.EXIT));
    }

    private void initTableMenuScreen() {
        InterfaceElement tableMenuScreen = this.elements.get(EnumOverlayElement.CUSTOM_MENU_SCREEN);
    }

    private void initTableMenuControls() {
        InterfaceElement tableMenuControls = this.elements.get(EnumOverlayElement.CUSTOM_MENU_CONTROLS);
    }

    private void initTableMenuSettings() {
        InterfaceElement tableMenuSettings = this.elements.get(EnumOverlayElement.CUSTOM_MENU_SETTINGS);
    }

    // TASK
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            Debug.debug("RECEIVED : " + received);
            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().isIn(EnumTargetTask.GAME_OVERLAY)) {
                if (received.getV3() instanceof Player) {
                    InterfaceElement tableIcon = this.elements.get(EnumOverlayElement.TABLE_ICON);

                    Player player = (Player) received.getV3();
                    List<EnumOverlayElement> icons = EnumOverlayElement.getOverlayElementByGameObject(player.getTank().getTankState().getType()).getSameIndexList();

                    if (icons.size() >= 3) {
                        tableIcon.doTask(new ImageElement(this.animatorOverlayData.getIconAnimator(icons.get(0)), "HitIcon", Element.PositionInBody.MIDDLE_MID));
                        tableIcon.doTask(new ImageElement(this.animatorOverlayData.getIconAnimator(icons.get(1)), "SpellIcon", Element.PositionInBody.MIDDLE_MID));
                        tableIcon.doTask(new ImageElement(this.animatorOverlayData.getIconAnimator(icons.get(2)), "BoxIcon", Element.PositionInBody.MIDDLE_MID));
                    }
                } else {
                    for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
                        entry.getValue().doTask(received.getV3());
                    }
                }
            }
        } else if (arg instanceof Pair) {
            Pair<EnumOverlayElement, EnumOverlayElement> received = (Pair<EnumOverlayElement, EnumOverlayElement>) arg;

            Debug.debug("RECEIVED : " + received);
            if (received.getV2() == EnumOverlayElement.EXIT) {
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.GAME, EnumWindow.INTERFACE));
            } else {
                this.elements.get(received.getV1()).stop();
                this.elements.get(received.getV2()).start();
            }
        }
    }

    // FUNCTIONS

    public void leave() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().leave();
        }
    }

    public void draw(Graphics g) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                entry.getValue().draw(g);
            }
        }
    }

    public void updateOverlay() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().update();
        }
    }


    public boolean event(int key, char c, EnumInput type) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                Object result = null;
                if (type == EnumInput.PRESSED) {
                    result = entry.getValue().eventPressed(key, c);
                } else if (type == EnumInput.RELEASED) {
                    result = entry.getValue().eventReleased(key, c);
                }
                if (result instanceof MessageChat) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.MESSAGE_SERVER, result));
                    return true;
                } else if (result instanceof Boolean) {
                    return (Boolean) result;
                }
            }
        }
        return false;
    }

    public boolean isOnFocus(int x, int y) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                if (entry.getValue().isOnFocus(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFocused() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                if (entry.getValue().isActivated()) {
                    return true;
                }
            }
        }
        return false;
    }
}
