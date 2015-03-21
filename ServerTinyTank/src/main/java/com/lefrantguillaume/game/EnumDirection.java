package com.lefrantguillaume.game;

/**
 * Created by Styve on 21/03/2015.
 */
public enum EnumDirection {
    NOTHING(0, 0), UP(200, 270), DOWN(208, 90), LEFT(203, 180), RIGHT(205, 0);

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
