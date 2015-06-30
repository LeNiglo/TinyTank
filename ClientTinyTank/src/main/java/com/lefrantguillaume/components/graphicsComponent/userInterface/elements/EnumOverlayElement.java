package com.lefrantguillaume.components.graphicsComponent.userInterface.elements;

/**
 * Created by andres_k on 23/06/2015.
 */
public enum EnumOverlayElement {
    // index
    NOTHING(0),
    CHAT(1),
    POP_ELEMENT(2),
    TABLE(3),

    // index
    POP_KILL(POP_ELEMENT.index),
    TABLE_NEW_ROUND(TABLE.index),
    TABLE_STAT(TABLE.index),

    // animator
    NEW_ROUND(0), STATE(0),

    // primitive
    IMAGE(0),
    STRING(0);


    private int index;

    EnumOverlayElement(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

    public EnumOverlayElement getTypeByIndex(){
        EnumOverlayElement[] enums = EnumOverlayElement.values();
        for (int i = 0 ; i < enums.length; ++i){
            EnumOverlayElement value = enums[i];
            if (value.getIndex() == this.index){
                return value;
            }
        }
        return NOTHING;
    }
}
