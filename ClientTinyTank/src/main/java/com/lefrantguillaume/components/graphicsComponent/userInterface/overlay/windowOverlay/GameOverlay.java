package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.windowOverlay;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.Overlay;
import com.lefrantguillaume.utils.configs.WindowConfig;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.utils.tools.ColorTools;
import com.lefrantguillaume.utils.tools.Debug;
import com.lefrantguillaume.utils.tools.StringTools;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.input.InputData;
import com.lefrantguillaume.components.graphicsComponent.sounds.MusicController;
import com.lefrantguillaume.components.graphicsComponent.sounds.SoundController;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.InterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.chat.ChatElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.generic.GenericElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.pop.StringPopElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.slider.SliderElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.table.TableActivateElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.table.TableAppearElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.table.TableMenuElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ButtonElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.Element;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.ImageElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements.StringElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.StringTimer;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.listElements.StringListElement;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
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
public class GameOverlay extends Overlay {


    public GameOverlay(InputData inputData) throws JSONException {
        super(inputData);

        this.initElements();
        this.initPreference();
    }

    @Override
    public void initElements() {
        this.elements.put(EnumOverlayElement.CHAT, new ChatElement(EnumOverlayElement.CHAT,
                new BodyRect(new Rectangle(0, WindowConfig.getSizeY() - 200, 388, 200), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));

        this.elements.put(EnumOverlayElement.POP_KILL, new StringPopElement(EnumOverlayElement.POP_KILL,
                new BodyRect(new Rectangle(WindowConfig.getSizeX() - 250, 0, 250, 400))));

        this.elements.put(EnumOverlayElement.TABLE_ROUND_NEW, new TableAppearElement(EnumOverlayElement.TABLE_ROUND_NEW,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 200, (WindowConfig.getSizeY() / 2) - 250, 400, 200))));
        this.elements.put(EnumOverlayElement.TABLE_ROUND_END, new TableAppearElement(EnumOverlayElement.TABLE_ROUND_END,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 200, (WindowConfig.getSizeY() / 2) - 250, 400, 200))));
        this.elements.put(EnumOverlayElement.TABLE_STAT, new TableActivateElement(EnumOverlayElement.TABLE_STAT,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 400, (WindowConfig.getSizeY() / 2) - 300, 700, 300), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), Input.KEY_TAB));
        this.elements.put(EnumOverlayElement.TABLE_ICON, new SliderElement(new GenericElement(EnumOverlayElement.TABLE_ICON,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - (192 / 2) + 40, WindowConfig.getSizeY() - 64, 192, 64), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(false, false), true, new boolean[]{true, true})));

        this.elements.put(EnumOverlayElement.GENERIC_USER_STAT, new GenericElement(EnumOverlayElement.GENERIC_USER_STAT,
                new BodyRect(new Rectangle(this.elements.get(EnumOverlayElement.TABLE_ICON).getBody().getMinX() - 170, WindowConfig.getSizeY() - 56, 170, 56), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(false, false), true, new boolean[]{true, true}));

        this.elements.put(EnumOverlayElement.TABLE_MENU_SCREEN, new TableMenuElement(EnumOverlayElement.TABLE_MENU_SCREEN, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 360, 210), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));
        this.elements.put(EnumOverlayElement.TABLE_MENU_CONTROLS, new TableMenuElement(EnumOverlayElement.TABLE_MENU_CONTROLS, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 400, 300), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));
        this.elements.put(EnumOverlayElement.TABLE_MENU_SETTINGS, new GenericElement(EnumOverlayElement.TABLE_MENU_SETTINGS,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(false, true), false, new boolean[]{true, true}));

        this.elements.put(EnumOverlayElement.TABLE_MENU, new GenericElement(EnumOverlayElement.TABLE_MENU, this.genericSendTask,
                new BodyRect(new Rectangle((WindowConfig.getSizeX() / 2) - 150, (WindowConfig.getSizeY() / 2) - 300, 300, 310), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(true, true), false, new boolean[]{true, true}));
    }

    @Override
    public void initElementsComponent(AnimatorOverlayData animatorOverlayData) {
        this.animatorOverlayData = animatorOverlayData;

        this.initTableNewRound();
        this.initTableEndRound();
        this.initUserStat();

        this.initTableMenu();
        this.initTableMenuScreen();
        this.initTableMenuControls();
        this.initTableMenuSettings();
    }

    private void initTableNewRound() {
        InterfaceElement tableNewRound = this.elements.get(EnumOverlayElement.TABLE_ROUND_NEW);
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.NEW_ROUND), EnumOverlayElement.NEW_ROUND.getValue() + ":" + EnumOverlayElement.NEW_ROUND.getValue(), Element.PositionInBody.MIDDLE_UP));
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.TIMER), EnumOverlayElement.NEW_ROUND.getValue() + ":" + EnumOverlayElement.TIMER.getValue(), Element.PositionInBody.MIDDLE_MID));
    }

    private void initTableEndRound() {
        InterfaceElement tableNewRound = this.elements.get(EnumOverlayElement.TABLE_ROUND_END);
        tableNewRound.doTask(new ImageElement(this.animatorOverlayData.getRoundAnimator(EnumOverlayElement.END_ROUND), EnumOverlayElement.END_ROUND.getValue() + ":" + EnumOverlayElement.END_ROUND.getValue(), Element.PositionInBody.MIDDLE_UP));
    }

    private void initUserStat() {
        InterfaceElement customUserStat = this.elements.get(EnumOverlayElement.GENERIC_USER_STAT);

        customUserStat.doTask(new ImageElement(new BodyRect(new Rectangle(customUserStat.getBody().getMinX() + 7, customUserStat.getBody().getMinY() + 5, 156, 19), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.USER_SHIELD.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        customUserStat.doTask(new ImageElement(new BodyRect(new Rectangle(customUserStat.getBody().getMinX() + 10, customUserStat.getBody().getMinY() + 7, 150, 15), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.USER_SHIELD.getValue(), Element.PositionInBody.LEFT_MID));
        customUserStat.doTask(new ImageElement(new BodyRect(new Rectangle(customUserStat.getBody().getMinX() + 7, customUserStat.getBody().getMinY() + 25, 156, 26), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.USER_LIFE.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        customUserStat.doTask(new ImageElement(new BodyRect(new Rectangle(customUserStat.getBody().getMinX() + 10, customUserStat.getBody().getMinY() + 28, 150, 20), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREEN)), EnumOverlayElement.USER_LIFE.getValue(), Element.PositionInBody.LEFT_MID));

        customUserStat.doTask(new Pair<>(EnumOverlayElement.USER_SHIELD, new Pair<>("cutBody", 0f)));
    }

    private void initTableMenu() {
        InterfaceElement tableMenu = this.elements.get(EnumOverlayElement.TABLE_MENU);
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
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.getGreenOrRed(this.overlayConfigs.getPreferenceValue(EnumOverlayElement.CHAT, i))), new StringTimer("chat"), Color.black,
                    screenId + ":" + EnumOverlayElement.CHAT.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.getGreenOrRed(this.overlayConfigs.getPreferenceValue(EnumOverlayElement.POP_KILL, i))), new StringTimer("kill event"), Color.black,
                    screenId + ":" + EnumOverlayElement.POP_KILL.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.getGreenOrRed(this.overlayConfigs.getPreferenceValue(EnumOverlayElement.TABLE_ICON, i))), new StringTimer("action bar"), Color.black,
                    screenId + ":" + EnumOverlayElement.TABLE_ICON.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
            tableMenuScreen.doTask(new ButtonElement(new StringElement(new BodyRect(null, ColorTools.getGreenOrRed(this.overlayConfigs.getPreferenceValue(EnumOverlayElement.GENERIC_USER_STAT, i))), new StringTimer("user stats"), Color.black,
                    screenId + ":" + EnumOverlayElement.GENERIC_USER_STAT.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.SCREEN));
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
        float posX = tableMenuSettings.getBody().getMinX();
        float posY = tableMenuSettings.getBody().getMinY();
        float sizeX = tableMenuSettings.getBody().getSizeX();

        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, sizeX, StringTools.charSizeY())), new StringTimer("Settings"), Color.black, Element.PositionInBody.MIDDLE_MID));

        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, tableMenuSettings.getBody().getSizeX() / 2, StringTools.charSizeY())), new StringTimer("Sounds"), Color.black, Element.PositionInBody.MIDDLE_MID));
        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, (int) (tableMenuSettings.getBody().getSizeX() / 1.1), StringTools.charSizeY())), new StringTimer(String.valueOf((int) (SoundController.getVolume() * 100))), Color.black, EnumOverlayElement.SOUNDS_VALUE.getValue(), Element.PositionInBody.RIGHT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 10, posY + 4, 202, 12), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.SOUNDS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 11, posY + 5, 200, 10), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.SOUNDS_GRAPH.getValue(), Element.PositionInBody.LEFT_MID));

        posY += 50;
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, tableMenuSettings.getBody().getSizeX() / 2, StringTools.charSizeY())), new StringTimer("Musics"), Color.black, Element.PositionInBody.MIDDLE_MID));
        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, (int) (tableMenuSettings.getBody().getSizeX() / 1.1), StringTools.charSizeY())), new StringTimer(String.valueOf((int) (MusicController.getVolume() * 100))), Color.black, EnumOverlayElement.MUSICS_VALUE.getValue(), Element.PositionInBody.RIGHT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 10, posY + 4, 202, 12), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.MUSICS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 11, posY + 5, 200, 10), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.MUSICS_GRAPH.getValue(), Element.PositionInBody.LEFT_MID));

        tableMenuSettings.doTask(new Pair<>(EnumOverlayElement.SOUNDS_GRAPH, new Pair<>("cutBody", SoundController.getVolume() / SoundController.getMaxVolume())));
        tableMenuSettings.doTask(new Pair<>(EnumOverlayElement.MUSICS_GRAPH, new Pair<>("cutBody", MusicController.getVolume() / MusicController.getMaxVolume())));
    }

    private void initTankIcons(EnumGameObject tankType) {
        InterfaceElement tableIcon = this.elements.get(EnumOverlayElement.TABLE_ICON);

        tableIcon.clearData();
        List<EnumOverlayElement> values = EnumOverlayElement.getOverlayElementByGameObject(tankType).getSameIndexList();

        if (values.size() >= 6) {
            tableIcon.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableIcon.getBody().getMinX(), tableIcon.getBody().getMinY(), 64, 64)), this.animatorOverlayData.getIconAnimator(values.get(0)), EnumOverlayElement.HIT.getValue(), Element.PositionInBody.MIDDLE_MID), values.get(3)));
            tableIcon.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableIcon.getBody().getMinX() + 64, tableIcon.getBody().getMinY(), 64, 64)), this.animatorOverlayData.getIconAnimator(values.get(1)), EnumOverlayElement.SPELL.getValue(), Element.PositionInBody.MIDDLE_MID), values.get(4)));
            tableIcon.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableIcon.getBody().getMinX() + 128, tableIcon.getBody().getMinY(), 64, 64)), this.animatorOverlayData.getIconAnimator(values.get(2)), EnumOverlayElement.BOX.getValue(), Element.PositionInBody.MIDDLE_MID), values.get(5)));

            tableIcon.doTask(new Pair<>(values.get(3), new StringListElement(new BodyRect(new Rectangle(tableIcon.getBody().getMinX() - 100, tableIcon.getBody().getMinY() - 100, 280, 100), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)))));
            tableIcon.doTask(new Pair<>(values.get(4), new StringListElement(new BodyRect(new Rectangle(tableIcon.getBody().getMinX() - 100, tableIcon.getBody().getMinY() - 100, 320, 100), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)))));
            tableIcon.doTask(new Pair<>(values.get(5), new StringListElement(new BodyRect(new Rectangle(tableIcon.getBody().getMinX() - 100, tableIcon.getBody().getMinY() - 100, 300, 100), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)))));

            tableIcon.doTask(new Pair<>(values.get(3), new StringElement(new StringTimer(this.overlayConfigs.getData(values.get(3))), Color.white, values.get(3).getValue(), Element.PositionInBody.MIDDLE_MID)));
            tableIcon.doTask(new Pair<>(values.get(4), new StringElement(new StringTimer(this.overlayConfigs.getData(values.get(4))), Color.white, values.get(4).getValue(), Element.PositionInBody.MIDDLE_MID)));
            tableIcon.doTask(new Pair<>(values.get(5), new StringElement(new StringTimer(this.overlayConfigs.getData(values.get(5))), Color.white, values.get(5).getValue(), Element.PositionInBody.MIDDLE_MID)));
        }
    }

    // TASK
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().isIn(EnumTargetTask.GAME_OVERLAY)) {
                Debug.debug("OVERLAY RECEIVED tuple: " + arg);
                if (received.getV3() instanceof Player) {
                    Player player = (Player) received.getV3();
                    this.initTankIcons(player.getTank().getTankState().getType());
                } else if (received.getV3() instanceof Pair && ((Pair) received.getV3()).getV1() instanceof EnumOverlayElement) {
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
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.GAME, EnumWindow.INTERFACE));
                } else {
                    if (this.elements.containsKey(received.getV1()) && this.elements.containsKey(received.getV2())) {
                        this.elements.get(received.getV1()).stop();
                        this.elements.get(received.getV2()).start();
                    }
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
        //Debug.debug("\n NEW EVENT: " + Input.getKeyName(key) + " (" + type + ")");
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                Object result = null;
                if (type == EnumInput.PRESSED) {
                    result = entry.getValue().eventPressed(key, c);
                } else if (type == EnumInput.RELEASED) {
                    result = entry.getValue().eventReleased(key, c);
                }
                //      Debug.debug("target: " + entry.getKey() + " -> result=" + result);
                if (result instanceof MessageChat) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME_OVERLAY, EnumTargetTask.MESSAGE_SERVER, result));
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
