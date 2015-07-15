package com.lefrantguillaume.components.taskComponent;

/**
 * Created by andres_k on 16/05/2015.
 */
public enum EnumTargetTask {
    UNKNOWN("UNKNOWN"), NETWORK("NETWORK"), WINDOWS("WINDOWS"),
    MESSAGE_SERVER("MESSAGE_SERVER", "NETWORK"), CONFIG_SERVER("CONFIG_SERVER", "NETWORK"),
    INPUT("INPUT", "WINDOWS"), GAME("GAME", "WINDOWS"), INTERFACE("INTERFACE", "WINDOWS"), ACCOUNT("ACCOUNT", "WINDOWS"),
    ACCOUNT_OVERLAY("ACCOUNT_OVERLAY", "ACCOUNT"),
    GAME_OVERLAY("GAME_OVERLAY", "GAME");

    private String dir;
    private String value;

    EnumTargetTask(String dir) {
        this.dir = dir;
        this.value = dir;
    }

    EnumTargetTask(String value, String dir) {
        this.value = value;
        this.dir = dir;
    }

    private String getDir() {
        return this.dir;
    }

    private String getValue() {
        return this.value;
    }

    public static EnumTargetTask getEnumByDir(String value) {
        EnumTargetTask[] enums = EnumTargetTask.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumTargetTask type = enums[i];
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static EnumTargetTask getEnumByValue(String value) {
        EnumTargetTask[] enums = EnumTargetTask.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumTargetTask type = enums[i];
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public boolean isIn(EnumTargetTask dir) {
        EnumTargetTask current = EnumTargetTask.getEnumByValue(this.value);

        while (!current.getValue().equals(current.getDir())) {
            if (current == dir) {
                return true;
            } else {
                current = EnumTargetTask.getEnumByDir(current.getDir());
            }
        }
        if (current == dir) {
            return true;
        }
        return false;
    }
}
