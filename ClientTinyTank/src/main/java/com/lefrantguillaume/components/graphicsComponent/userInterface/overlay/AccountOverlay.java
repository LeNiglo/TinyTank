package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;


import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.input.InputData;
import com.lefrantguillaume.components.graphicsComponent.sounds.MusicController;
import com.lefrantguillaume.components.graphicsComponent.sounds.SoundController;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.generic.GenericElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.table.TableMenuElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ButtonElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ImageElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import com.lefrantguillaume.utils.configs.WindowConfig;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.utils.tools.ColorTools;
import com.lefrantguillaume.utils.tools.Debug;
import com.lefrantguillaume.utils.tools.StringTools;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by andres_k on 20/06/2015.
 */
public class AccountOverlay extends Overlay {


    public AccountOverlay(InputData inputData) throws JSONException {
        super(inputData);

        this.initElements();
        this.initPreference();
    }

    @Override
    public void initElements() {
/*
        this.elements.put(EnumOverlayElement.CHAT, new ChatElement(EnumOverlayElement.CHAT,
                new BodyRect(new Rectangle(0, WindowConfig.getSizeY() - 200, 388, 200), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));
*/

        this.elements.put(EnumOverlayElement.TABLE_MENU_CONTROLS, new TableMenuElement(EnumOverlayElement.TABLE_MENU_CONTROLS, this.genericSendTask,
                new BodyRect(new Rectangle(WindowConfig.getSizeX() - 400, 0, 400, 300), ColorTools.get(ColorTools.Colors.SOFT_BLACK))));
        this.elements.put(EnumOverlayElement.TABLE_MENU_SETTINGS, new GenericElement(EnumOverlayElement.TABLE_MENU_SETTINGS,
                new BodyRect(new Rectangle(WindowConfig.getSizeX() - 300, 0, 300, 310), ColorTools.get(ColorTools.Colors.SOFT_BLACK)), new Pair<>(false, true), false, new boolean[]{true, true}));

        this.elements.put(EnumOverlayElement.TABLE_MENU, new GenericElement(EnumOverlayElement.TABLE_MENU, this.genericSendTask,
                new BodyRect(new Rectangle(WindowConfig.getSizeX() - 300, 0, 300, 240), ColorTools.get(ColorTools.Colors.SOFT_BLACK)), new Pair<>(true, true), false, new boolean[]{true, true}));
    }

    @Override
    public void initElementsComponent(AnimatorOverlayData animatorOverlayData) {
        this.animatorOverlayData = animatorOverlayData;

        this.initTableMenu();
        this.initTableMenuControls();
        this.initTableMenuSettings();
    }

    private void initTableMenu() {
        InterfaceElement tableMenu = this.elements.get(EnumOverlayElement.TABLE_MENU);
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 20, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLACK)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.CONTROLS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_CONTROLS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 90, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLACK)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.SETTINGS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_SETTINGS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 160, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLACK)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.EXIT), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.EXIT));
    }

    private void initTableMenuControls() {
        InterfaceElement tableMenuControls = this.elements.get(EnumOverlayElement.TABLE_MENU_CONTROLS);

        tableMenuControls.doTask(new ButtonElement(new StringElement(new StringTimer("Controls"), Color.white,
                EnumOverlayElement.CONTROLS.getValue() + ":" + EnumOverlayElement.CONTROLS.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.BUTTON));

        for (Map.Entry<EnumInput, String> entry : this.inputData.getAvailableInput().entrySet()) {
            tableMenuControls.doTask(new ButtonElement(new StringElement(new StringTimer(entry.getKey().getValue() + ":" +
                    StringTools.duplicateString(" ", 14 - entry.getKey().getValue().length()) + entry.getValue() +
                    StringTools.duplicateString(" ", 18 - entry.getValue().length())), Color.white,
                    EnumOverlayElement.CONTROLS.getValue() + ":" + entry.getKey().getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.CONTROLS));
        }
    }

    private void initTableMenuSettings() {
        InterfaceElement tableMenuSettings = this.elements.get(EnumOverlayElement.TABLE_MENU_SETTINGS);
        float posX = tableMenuSettings.getBody().getMinX();
        float posY = tableMenuSettings.getBody().getMinY();
        float sizeX = tableMenuSettings.getBody().getSizeX();

        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, sizeX, StringTools.charSizeY())), new StringTimer("Settings"), Color.white, Element.PositionInBody.MIDDLE_MID));

        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, tableMenuSettings.getBody().getSizeX() / 2, StringTools.charSizeY())), new StringTimer("Sounds"), Color.white, Element.PositionInBody.MIDDLE_MID));
        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, (int) (tableMenuSettings.getBody().getSizeX() / 1.1), StringTools.charSizeY())), new StringTimer(String.valueOf((int) (SoundController.getVolume() * 100))), Color.black, EnumOverlayElement.SOUNDS_VALUE.getValue(), Element.PositionInBody.RIGHT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 10, posY + 4, 202, 12), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.SOUNDS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 11, posY + 5, 200, 10), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.SOUNDS_GRAPH.getValue(), Element.PositionInBody.LEFT_MID));

        posY += 50;
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, tableMenuSettings.getBody().getSizeX() / 2, StringTools.charSizeY())), new StringTimer("Musics"), Color.white, Element.PositionInBody.MIDDLE_MID));
        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, (int) (tableMenuSettings.getBody().getSizeX() / 1.1), StringTools.charSizeY())), new StringTimer(String.valueOf((int) (MusicController.getVolume() * 100))), Color.black, EnumOverlayElement.MUSICS_VALUE.getValue(), Element.PositionInBody.RIGHT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 10, posY + 4, 202, 12), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.MUSICS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 11, posY + 5, 200, 10), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.MUSICS_GRAPH.getValue(), Element.PositionInBody.LEFT_MID));

        tableMenuSettings.doTask(new Pair<>(EnumOverlayElement.SOUNDS_GRAPH, new Pair<>("cutBody", SoundController.getVolume() / SoundController.getMaxVolume())));
        tableMenuSettings.doTask(new Pair<>(EnumOverlayElement.MUSICS_GRAPH, new Pair<>("cutBody", MusicController.getVolume() / MusicController.getMaxVolume())));
    }

    // TASK
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().isIn(EnumTargetTask.ACCOUNT_OVERLAY)) {

                Debug.debug("OVERLAY RECEIVED tuple: " + arg);
                if (received.getV3() instanceof Pair && ((Pair) received.getV3()).getV1() instanceof EnumOverlayElement) {
                    Pair<EnumOverlayElement, Object> task = (Pair<EnumOverlayElement, Object>) received.getV3();

                    List<EnumOverlayElement> targets = new ArrayList<>();
                    targets.addAll(EnumOverlayElement.getChildren(task.getV1()));
                    for (EnumOverlayElement target : targets) {
                        Debug.debug("CHIDL: " + targets.size() + " -> send to " + target);
                        if (this.elements.containsKey(target)) {
                            this.elements.get(target).doTask(task.getV2());
                        }
                    }
                } else {
                    Debug.debug("\n*************\nWARNING!\nyou shouldn't call this method like this : " + received.getV3());
                    for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
                        entry.getValue().doTask(received.getV3());
                    }
                }
            }
        } else if (arg instanceof Pair) {
            Debug.debug("OVERLAY RECEIVED pair: " + arg);
            if (((Pair) arg).getV1() instanceof EnumOverlayElement && ((Pair) arg).getV2() instanceof EnumOverlayElement) {
                Pair<EnumOverlayElement, EnumOverlayElement> received = (Pair<EnumOverlayElement, EnumOverlayElement>) arg;

                if (received.getV2() == EnumOverlayElement.EXIT && this.elements.containsKey(received.getV1())) {
                    this.elements.get(received.getV1()).stop();
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.ACCOUNT_OVERLAY, EnumTargetTask.ACCOUNT, EnumWindow.EXIT));
                } else if (this.elements.containsKey(received.getV1()) && this.elements.containsKey(received.getV2())) {
                    this.elements.get(received.getV1()).stop();
                    this.elements.get(received.getV2()).start();
                }
            } else if (((Pair) arg).getV2() instanceof Pair) {
                Pair<EnumOverlayElement, Pair> received = (Pair<EnumOverlayElement, Pair>) arg;
                if (this.elements.containsKey(received.getV1())) {
                    this.elements.get(received.getV1()).doTask(received.getV2());
                    this.overlayConfigs.setAvailableInput(received.getV1(), this.elements.get(received.getV1()).getReachable());
                }
            }
        }
    }

    // FUNCTIONS

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
                // TODO: A ENVOYER AUX AUTRE JOUEURS
                if (result instanceof MessageChat) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.ACCOUNT_OVERLAY, EnumTargetTask.UNKNOWN, result));
                    return true;
                } else if (result instanceof Boolean) {
                    return (Boolean) result;
                } else if (result instanceof Pair) {
                    Pair<Object, Object> task = (Pair<Object, Object>) result;

                    if (task.getV1() instanceof EnumInput && task.getV2() instanceof String) {
                        if (this.inputData.setAvailableInput((EnumInput) task.getV1(), (String) task.getV2())) {
                            this.elements.get(EnumOverlayElement.TABLE_MENU_CONTROLS).doTask(task.getV2());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
