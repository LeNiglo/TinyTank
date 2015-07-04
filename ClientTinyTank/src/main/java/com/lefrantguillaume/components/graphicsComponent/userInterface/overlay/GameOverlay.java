package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.ColorTools;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.input.InputData;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.*;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ButtonElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ImageElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.util.*;

/**
 * Created by andres_k on 20/06/2015.
 */
public class GameOverlay extends Observable implements Observer {
    private int current;

    private Map<EnumOverlayElement, InterfaceElement> elements;
    private AnimatorOverlayData animatorOverlayData;
    private GenericSendTask genericSendTask;
    private InputData inputData;

    public GameOverlay(InputData inputData) {
        this.current = 0;
        this.inputData = inputData;

        this.genericSendTask = new GenericSendTask();
        this.genericSendTask.addObserver(this);

        this.elements = new LinkedHashMap<>();

        this.elements.put(EnumOverlayElement.CHAT, new ChatElement(EnumOverlayElement.CHAT,
                new BodyRect(new Rectangle(0, WindowConfig.getSizeY() - 200, 408, 200), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));

        this.elements.put(EnumOverlayElement.POP_KILL, new StringPopElement(EnumOverlayElement.POP_KILL,
                new BodyRect(new Rectangle(WindowConfig.getSizeX() - 250, 0, 250, 400))));

        this.elements.put(EnumOverlayElement.TABLE_NEW_ROUND, new TableAppearElement(EnumOverlayElement.TABLE_NEW_ROUND,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 368, (WindowConfig.getSizeY() / 2) - 72, 700, 300))));
        this.elements.put(EnumOverlayElement.TABLE_STAT, new TableActivateElement(EnumOverlayElement.TABLE_STAT,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 400, (WindowConfig.getSizeY() / 2) - 300, 700, 300), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), Input.KEY_TAB));
        this.elements.put(EnumOverlayElement.TABLE_ICON, new TableElement(EnumOverlayElement.TABLE_ICON,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - (192 / 2), WindowConfig.getSizeY() - 64, 192, 64), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), true, new boolean[]{true, true}));

        this.elements.put(EnumOverlayElement.TABLE_MENU_SCREEN, new TableMenuElement(EnumOverlayElement.TABLE_MENU_SCREEN, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 360, 210), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));
        this.elements.put(EnumOverlayElement.TABLE_MENU_CONTROLS, new TableMenuElement(EnumOverlayElement.TABLE_MENU_CONTROLS, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 400, 300), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));
        this.elements.put(EnumOverlayElement.TABLE_MENU_SETTINGS, new TableMenuElement(EnumOverlayElement.TABLE_MENU_SETTINGS, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));

        this.elements.put(EnumOverlayElement.CUSTOM_MENU, new CustomElement(EnumOverlayElement.CUSTOM_MENU, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), false, new boolean[]{true, true}));
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
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.NEW_ROUND), EnumOverlayElement.NEW_ROUND.getValue(), Element.PositionInBody.MIDDLE_UP));
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.STATE), EnumOverlayElement.NEW_ROUND.getValue(), Element.PositionInBody.MIDDLE_MID));
    }

    private void initTableMenu() {
        InterfaceElement tableMenu = this.elements.get(EnumOverlayElement.CUSTOM_MENU);
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 20, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.SCREEN), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_SCREEN));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 90, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.CONTROLS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_CONTROLS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 160, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.SETTINGS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_SETTINGS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 230, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.EXIT), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.EXIT));
    }

    private void initTableMenuScreen() {
        InterfaceElement tableMenuScreen = this.elements.get(EnumOverlayElement.TABLE_MENU_SCREEN);

        for (int i = 0; i < 2; ++i) {
            String screenId = EnumOverlayElement.SCREEN.getValue() + String.valueOf(i + 1);
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new StringTimer("Render Screen " + String.valueOf(i + 1)), Color.black,
                    screenId + ":" + screenId, Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.BUTTON));
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.get(ColorTools.Colors.TRANSPARENT_GREEN)), new StringTimer("chat"), Color.black,
                    screenId + ":" + EnumOverlayElement.CHAT.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.get(ColorTools.Colors.TRANSPARENT_GREEN)), new StringTimer("kill event"), Color.black,
                    screenId + ":" + EnumOverlayElement.POP_KILL.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.get(ColorTools.Colors.TRANSPARENT_GREEN)), new StringTimer("action bar"), Color.black,
                    screenId + ":" + EnumOverlayElement.TABLE_ICON.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
        }
        this.elements.get(EnumOverlayElement.TABLE_MENU_SCREEN).doTask(this.current);
    }

    private void initTableMenuControls() {
        InterfaceElement tableMenuControls = this.elements.get(EnumOverlayElement.TABLE_MENU_CONTROLS);

        tableMenuControls.doTask(new ButtonElement(new StringElement(new StringTimer("Controls"), Color.black,
                EnumOverlayElement.CONTROLS.getValue() + ":" + EnumOverlayElement.CONTROLS.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.BUTTON));

        for (Map.Entry<EnumInput, String> entry : this.inputData.getAvailableInput().entrySet()) {
            tableMenuControls.doTask(new ButtonElement(new StringElement(new StringTimer(entry.getKey().getValue() + ":" +
                    StringTools.duplicateString(" ", 14 - entry.getKey().getValue().length()) + entry.getValue() +
                    StringTools.duplicateString(" ", 18 - entry.getValue().length())), Color.black,
                    EnumOverlayElement.CONTROLS.getValue() + ":" + entry.getKey().getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.CONTROLS));
        }
    }

    private void initTableMenuSettings() {
        InterfaceElement tableMenuSettings = this.elements.get(EnumOverlayElement.TABLE_MENU_SETTINGS);
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
            if (((Pair) arg).getV2() instanceof EnumOverlayElement) {
                Pair<EnumOverlayElement, EnumOverlayElement> received = (Pair<EnumOverlayElement, EnumOverlayElement>) arg;

                Debug.debug("RECEIVED : " + received);
                if (received.getV2() == EnumOverlayElement.EXIT) {
                    this.elements.get(received.getV1()).stop();
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.GAME, EnumWindow.INTERFACE));
                } else {
                    this.elements.get(received.getV1()).stop();
                    this.elements.get(received.getV2()).start();
                }
            } else if (((Pair) arg).getV2() instanceof Pair) {
                Pair<EnumOverlayElement, Pair> received = (Pair<EnumOverlayElement, Pair>) arg;
                Debug.debug("received: " + received);
                this.elements.get(received.getV1()).doTask(received.getV2());
                //todo à envoyer à une classe qui gère les accès via un fichier
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


    public void doTask(Object task) {
        if (task instanceof Integer) {
            if ((Integer) task == EnumInput.OVERLAY_1.getIndex() || (Integer) task == EnumInput.OVERLAY_2.getIndex()) {
                String value = EnumInput.getEnumByIndex((Integer) task).getValue();
                this.current = Integer.valueOf(value.substring(value.indexOf("_") + 1)) - 1;
                this.current = (this.current < 0 ? 0 : this.current);
                this.current = (this.current > 1 ? 1 : this.current);
                this.elements.get(EnumOverlayElement.TABLE_MENU_SCREEN).doTask(this.current);
            }
        }
    }

    public boolean event(int key, char c, EnumInput type) {
        Debug.debug("\n NEW EVENT: " + Input.getKeyName(key) + " (" + type + ")");
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                Object result = null;
                if (type == EnumInput.PRESSED) {
                    result = entry.getValue().eventPressed(key, c);
                } else if (type == EnumInput.RELEASED) {
                    result = entry.getValue().eventReleased(key, c);
                }
                Debug.debug("target: " + entry.getKey() + " -> result=" + result);
                if (result instanceof MessageChat) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.MESSAGE_SERVER, result));
                    return true;
                } else if (result instanceof Boolean) {
                    return (Boolean) result;
                } else if (result instanceof Pair) {
                    Pair<Object, Object> task = (Pair<Object, Object>) result;

                    if (task.getV1() instanceof EnumInput && task.getV2() instanceof String) {
                        if (this.inputData.setAvailableInput((EnumInput) task.getV1(), (String) task.getV2())){
                            this.elements.get(EnumOverlayElement.TABLE_MENU_CONTROLS).doTask(task.getV2());
                        }
                    }
                    return true;
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
