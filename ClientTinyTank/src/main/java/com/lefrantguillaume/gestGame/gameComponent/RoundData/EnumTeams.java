package com.lefrantguillaume.gestGame.gameComponent.RoundData;

/**
 * Created by andres_k on 19/03/2015.
 */
public enum EnumTeams {
    BLUE("Blue"), RED("Red"), GREEN("Green");

    private String value;

    EnumTeams(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
