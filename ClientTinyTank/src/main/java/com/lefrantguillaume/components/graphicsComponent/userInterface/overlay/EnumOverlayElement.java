package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 23/06/2015.
 */
public enum EnumOverlayElement {
    // index
    NOTHING("nothing", -1),
    CHAT("chat", 1),
    POP_ELEMENT("popElement", 2),
    TABLE("table", 3),
    CUSTOM("custom", 4),

    // index
    POP_KILL("popKill", POP_ELEMENT.index),
    TABLE_NEW_ROUND("tableNewRound", TABLE.index),
    TABLE_END_ROUND("tableEndRound", TABLE.index),
    TABLE_STAT("tableStat", TABLE.index),
    TABLE_ICON("tableIcon", TABLE.index),
    CUSTOM_MENU("customMenu", CUSTOM.index),
    TABLE_MENU_SETTINGS("tableMenuSettings", CUSTOM.index),
    TABLE_MENU_CONTROLS("tableMenuControls", CUSTOM.index),
    TABLE_MENU_SCREEN("tableMenuScreen", CUSTOM.index),


    // primitive
    IMAGE("image"),
    STRING("string"),
    BUTTON("button"),

    // roundAnimator
    NEW_ROUND("newRound"), END_ROUND("endRound"), TIMER("stat"),

    // menuAnimator
    EXIT("exit"), SETTINGS("settings"), CONTROLS("controls"), SCREEN("screen"),

    // iconAnimator
    HIT("hit"), SPELL("spell"), BOX("box"),
    TIGER("tiger", 5), SNIPER("sniper", 6), RUSHER("rusher", 7),
    TIGER_HIT(TIGER.value + HIT.value, TIGER.index), TIGER_SPELL(TIGER.value + SPELL.value, TIGER.index), TIGER_BOX(TIGER.value + BOX.value, TIGER.index),
    SNIPER_HIT(SNIPER.value + HIT.value, SNIPER.index), SNIPER_SPELL(SNIPER.value + SPELL.value, SNIPER.index), SNIPER_BOX(SNIPER.value + BOX.value, SNIPER.index),
    RUSHER_HIT(RUSHER.value + HIT.value, RUSHER.index), RUSHER_SPELL(RUSHER.value + SPELL.value, RUSHER.index), RUSHER_BOX(RUSHER.value + BOX.value, RUSHER.index);


    private int index;
    private String value;

    EnumOverlayElement(String value) {
        this.index = 0;
        this.value = value;
    }

    EnumOverlayElement(String value, int index) {
        this.value = value;
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public EnumOverlayElement getEnumByIndex() {
        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0; i < enums.length; ++i) {
            EnumOverlayElement item = enums[i];
            if (item.getIndex() == this.index) {
                return item;
            }
        }
        return NOTHING;
    }

    public static EnumOverlayElement getEnumByValue(String value) {
        Debug.debug("try to find '" + value + "'");
        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0; i < enums.length; ++i) {
            EnumOverlayElement item = enums[i];
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return NOTHING;
    }

    public List<EnumOverlayElement> getSameIndexList() {
        List<EnumOverlayElement> result = new ArrayList<>();

        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0; i < enums.length; ++i) {
            EnumOverlayElement item = enums[i];
            if (item.getIndex() == this.index && item != this) {
                result.add(item);
            }
        }
        return result;
    }

    public static EnumOverlayElement getOverlayElementByGameObject(EnumGameObject gameObject) {
        if (gameObject == EnumGameObject.TIGER) {
            return EnumOverlayElement.TIGER;
        } else if (gameObject == EnumGameObject.SNIPER) {
            return EnumOverlayElement.SNIPER;
        } else if (gameObject == EnumGameObject.RUSHER) {
            return EnumOverlayElement.RUSHER;
        }
        return NOTHING;
    }

    public String getValue(){
        return this.value;
    }
}
