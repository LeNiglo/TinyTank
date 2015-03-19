package com.lefrantguillaume.gameComponent.playerData;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Team {
    private int idTeam;
    private String name;

    public Team(int idTeam, String name) {
        this.idTeam = idTeam;
        this.name = name;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
