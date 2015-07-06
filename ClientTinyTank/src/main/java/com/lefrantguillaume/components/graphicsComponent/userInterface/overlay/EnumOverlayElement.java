package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

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
    GENERIC("generic", 4),

    // index
    POP_KILL(POP_ELEMENT.value + "Kill", POP_ELEMENT.index),
    TABLE_ROUND(TABLE.value + "Round", TABLE.index),
    TABLE_ROUND_NEW(TABLE_ROUND.value + "New", TABLE_ROUND.index),
    TABLE_ROUND_END(TABLE_ROUND.value + "END", TABLE_ROUND.index),
    TABLE_STAT(TABLE.value + "Stat", TABLE.index),
    TABLE_ICON(TABLE.value + "Icon", TABLE.index),
    GENERIC_USER_STAT(GENERIC.value + "UserStat", GENERIC.index),
    TABLE_MENU(TABLE.value + "Menu", TABLE.index),
    TABLE_MENU_SETTINGS(TABLE_MENU.value + "Settings", TABLE_MENU.index),
    TABLE_MENU_CONTROLS(TABLE_MENU.value + "Controls", TABLE_MENU.index),
    TABLE_MENU_SCREEN(TABLE_MENU.value + "Screen", TABLE_MENU.index),


    // primitive
    IMAGE("image"),
    STRING("string"),
    BUTTON("button"),
    BORDER("border"),

    // roundAnimator
    NEW_ROUND("newRound"), END_ROUND("endRound"), TIMER("stat"),

    // menuAnimator
    EXIT("exit"), SETTINGS("settings"), CONTROLS("controls"), SCREEN("screen"),

    // iconAnimator
    HIT("hit"), SPELL("spell"), BOX("box"), COMMENT("comment"),
    TIGER("tiger", 5), SNIPER("sniper", 6), RUSHER("rusher", 7),
    TIGER_HIT(TIGER.value + HIT.value, TIGER.index), TIGER_SPELL(TIGER.value + SPELL.value, TIGER.index), TIGER_BOX(TIGER.value + BOX.value, TIGER.index),
    SNIPER_HIT(SNIPER.value + HIT.value, SNIPER.index), SNIPER_SPELL(SNIPER.value + SPELL.value, SNIPER.index), SNIPER_BOX(SNIPER.value + BOX.value, SNIPER.index),
    RUSHER_HIT(RUSHER.value + HIT.value, RUSHER.index), RUSHER_SPELL(RUSHER.value + SPELL.value, RUSHER.index), RUSHER_BOX(RUSHER.value + BOX.value, RUSHER.index),

    TIGER_HIT_COMMENT(TIGER.value + HIT.value + COMMENT.value, TIGER.index), TIGER_SPELL_COMMENT(TIGER.value + SPELL.value + COMMENT.value, TIGER.index), TIGER_BOX_COMMENT(TIGER.value + BOX.value + COMMENT.value, TIGER.index),
    SNIPER_HIT_COMMENT(SNIPER.value + HIT.value + COMMENT.value, SNIPER.index), SNIPER_SPELL_COMMENT(SNIPER.value + SPELL.value + COMMENT.value, SNIPER.index), SNIPER_BOX_COMMENT(SNIPER.value + BOX.value + COMMENT.value, SNIPER.index),
    RUSHER_HIT_COMMENT(RUSHER.value + HIT.value + COMMENT.value, RUSHER.index), RUSHER_SPELL_COMMENT(RUSHER.value + SPELL.value + COMMENT.value, RUSHER.index), RUSHER_BOX_COMMENT(RUSHER.value + BOX.value + COMMENT.value, RUSHER.index),

    // item
    USER_LIFE("life"), USER_SHIELD("shield");

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

    public static List<EnumOverlayElement> getChildren(EnumOverlayElement target){
        List<EnumOverlayElement> targets = new ArrayList<>();

        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0; i < enums.length; ++i) {
            EnumOverlayElement item = enums[i];
            if (item.getValue().contains(target.getValue())) {
                targets.add(item);
            }
        }
        return targets;
    }

    public String getValue(){
        return this.value;
    }
}
