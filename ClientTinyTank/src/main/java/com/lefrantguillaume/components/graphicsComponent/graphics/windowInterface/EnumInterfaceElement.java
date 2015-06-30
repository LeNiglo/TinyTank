package com.lefrantguillaume.components.graphicsComponent.graphics.windowInterface;

/**
 * Created by andres_k on 20/03/2015.
 */
public enum EnumInterfaceElement {
    TIGER(0, "tiger"), SNIPER(1, "sniper"), RUSHER(2, "rusher"),
    RANK("rank"), BACKGROUND_1("background_1"),
    PREV("prev"), NEXT("next");

    private final String value;
    private final int index;

    EnumInterfaceElement(int index, String value) {
        this.index = index;
        this.value = value;
    }

    EnumInterfaceElement(String value) {
        this.index = -1;
        this.value = value;
    }

    public int getIndex(){
        return this.index;
    }

    public String getValue() {
        return this.value;
    }
}
