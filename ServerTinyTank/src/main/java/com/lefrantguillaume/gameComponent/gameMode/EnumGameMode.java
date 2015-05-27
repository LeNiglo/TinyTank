package com.lefrantguillaume.gameComponent.gameMode;


/**
 * Created by Styve on 19/03/2015.
 */
public enum EnumGameMode {
    Nothing(-1, "Nothing"), FreeForAll(0, "FreeForAll"), TeamDeathMatch(1, "TeamDeathMatch"), TouchDawn(2, "TouchDawn"), Kingdom(3, "Kingdom");

    private int index;
    private String value;

    EnumGameMode(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex(){
        return this.index;
    }

    public String getValue(){
        return this.value;
    }

    public static EnumGameMode getEnumByValue(String value){
        EnumGameMode[] enums = EnumGameMode.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumGameMode type = enums[i];
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        return Nothing;
    }
}
