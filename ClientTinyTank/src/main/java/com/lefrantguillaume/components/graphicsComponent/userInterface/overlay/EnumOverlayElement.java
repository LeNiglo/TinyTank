package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 23/06/2015.
 */
public enum EnumOverlayElement {
    // index
    NOTHING(-1),
    CHAT(1),
    POP_ELEMENT(2),
    TABLE(3),

    // index
    POP_KILL(POP_ELEMENT.index),
    TABLE_NEW_ROUND(TABLE.index),
    TABLE_STAT(TABLE.index),
    TABLE_ICON(TABLE.index),


    // primitive
    IMAGE,
    STRING,

    // roundAnimator
    NEW_ROUND, STATE,


    // iconAnimator
    TIGER(4), SNIPER(5), RUSHER(6),
    TIGER_HIT(TIGER.index), TIGER_SPELL(TIGER.index), TIGER_BOX(TIGER.index),
    SNIPER_HIT(SNIPER.index), SNIPER_SPELL(SNIPER.index), SNIPER_BOX(SNIPER.index),
    RUSHER_HIT(RUSHER.index), RUSHER_SPELL(RUSHER.index), RUSHER_BOX(RUSHER.index);


    private int index;

    EnumOverlayElement() {
        this.index = 0;
    }

    EnumOverlayElement(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public EnumOverlayElement getTypeByIndex() {
        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0; i < enums.length; ++i) {
            EnumOverlayElement value = enums[i];
            if (value.getIndex() == this.index) {
                return value;
            }
        }
        return NOTHING;
    }

    public List<EnumOverlayElement> getSameIndexList() {
        List<EnumOverlayElement> result = new ArrayList<>();

        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0; i < enums.length; ++i) {
            EnumOverlayElement value = enums[i];
            if (value.getIndex() == this.index && value != this) {
                result.add(value);
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
}
