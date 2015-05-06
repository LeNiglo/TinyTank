package com.lefrantguillaume.gameComponent.playerData.data;

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

    public UUID getId() {
        return UUID.fromString(idUser);
    }

    // SETTERS
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
