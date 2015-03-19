package com.lefrantguillaume.gameComponent.actions;

import org.newdawn.slick.Input;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumDirection {
    NOTHING(0, 0), UP(Input.KEY_UP, 270), DOWN(Input.KEY_DOWN, 90), LEFT(Input.KEY_LEFT, 180), RIGHT(Input.KEY_RIGHT, 0);

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
