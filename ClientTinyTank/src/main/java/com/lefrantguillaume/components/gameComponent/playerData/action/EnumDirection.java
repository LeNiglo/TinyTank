package com.lefrantguillaume.components.gameComponent.playerData.action;

import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumDirection {
    NOTHING(-1, 0), UP(EnumInput.MOVE_UP.getIndex(), 270), DOWN(EnumInput.MOVE_DOWN.getIndex(), 90), LEFT(EnumInput.MOVE_LEFT.getIndex(), 180), RIGHT(EnumInput.MOVE_RIGHT.getIndex(), 0);

    private final int value;
    private final float angle;

    EnumDirection(int value, int angle) {
        this.value = value;
        this.angle = angle;
    }

    public int getValue() {
        return this.value;
    }

    public float getAngle() {
        return this.angle;
    }

    public static EnumDirection getDirectionByValue(int value) {
        EnumDirection[] enums = EnumDirection.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumDirection type = enums[i];
            if (value == type.getValue()) {
                return type;
            }
        }
        return NOTHING;
    }

    public static float getAngleByValue(int value) {
        EnumDirection[] enums = EnumDirection.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumDirection type = enums[i];
            if (value == type.getValue()) {
                return type.getAngle();
            }
        }
        return 0f;
    }
}
