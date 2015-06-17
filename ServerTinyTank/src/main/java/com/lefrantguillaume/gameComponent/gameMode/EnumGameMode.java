package com.lefrantguillaume.gameComponent.gameMode;


/**
 * Created by Styve on 19/03/2015.
 */
public enum EnumGameMode {
    Nothing("Nothing"), FreeForAll("FreeForAll"), TeamDeathMatch("TeamDeathMatch"), TouchDown("TouchDown"), Kingdom("Kingdom");

    private String value;

    EnumGameMode(String value) {
        this.value = value;
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
