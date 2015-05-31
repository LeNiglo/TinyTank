package com.lefrantguillaume.components.gameComponent.playerData.data;

import java.util.UUID;

/**
 * Created by andres_k on 17/03/2015.
 */
public class User {
    private String pseudo;
    private final String idUser;
    public User(String pseudo, String idUser) {
        this.pseudo = pseudo;
        this.idUser = idUser;
    }

    // GETTERS
    public String getIdUser() {
        return this.idUser;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getId() {
        return this.idUser;
    }

    // SETTERS
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
