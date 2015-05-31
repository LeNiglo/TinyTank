package com.lefrantguillaume.components.taskComponent;

/**
 * Created by andres_k on 16/05/2015.
 */
public enum EnumTargetTask {
    UNKNOWN("UNKNOWN"), NETWORK("NETWORK"), WINDOWS("WINDOWS"),
    MESSAGE_SERVER("NETWORK"), CONFIG_SERVER("NETWORK"),
    INPUT("WINDOWS"), GAME("WINDOWS"), INTERFACE("WINDOWS"), ACCOUNT("WINDOWS");

    private String dir;

    EnumTargetTask(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return this.dir;
    }

    public static EnumTargetTask getEnumByValue(String value) {
        EnumTargetTask[] enums = EnumTargetTask.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumTargetTask type = enums[i];
            if (value.equals(type.getDir())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public boolean isIn(EnumTargetTask dir) {
        if (EnumTargetTask.getEnumByValue(this.dir).equals(dir)) {
            return true;
        } else {
            return false;
        }
    }
}
