package com.lefrantguillaume.components.graphicsComponent.input;

/**
 * Created by andres_k on 16/03/2015.
 */
public enum EnumInput {
    NOTHING(-3, "NOTHING"),
    RELEASED(-2, "RELEASED"), PRESSED(-1, "PRESSED"),
    MOVE_UP(0, "MOVE_UP"), MOVE_DOWN(1, "MOVE_DOWN"), MOVE_RIGHT(2, "MOVE_RIGHT"), MOVE_LEFT(3, "MOVE_LEFT"),
    PUT_OBJECT(4, "PUT_OBJECT"), SHOOT(5, "SHOOT"), SPELL(6, "SPELL"),
    ESCAPE(7, "ESCAPE");


    private final int index;
    private final String value;

    EnumInput(int index, String value)
    {
        this.index = index;
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public int getIndex(){
        return this.index;
    }

    public static EnumInput getEnumByValue(String value){
        EnumInput[] enums = EnumInput.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumInput type = enums[i];
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        return NOTHING;
    }

    public static int getIndexByValue(String value){
        EnumInput[] enums = EnumInput.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumInput type = enums[i];
            if (value.equals(type.getValue())) {
                return type.getIndex();
            }
        }
        return 0;
    }
}
