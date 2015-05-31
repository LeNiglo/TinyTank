package com.lefrantguillaume.components.gameComponent.RoundData;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Team {
    private EnumTeams team;

    public Team(EnumTeams team) {
        this.team = team;
    }

    public EnumTeams getTeam() {
        return team;
    }

    public void setTeam(EnumTeams team) {
        this.team = team;
    }
}
