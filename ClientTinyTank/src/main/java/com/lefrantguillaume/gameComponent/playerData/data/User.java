package com.lefrantguillaume.gameComponent.playerData.data;

import java.util.UUID;

/**
 * Created by andres_k on 17/03/2015.
 */
public class User {
    private String pseudo;
    private final String idUser;
    private final UUID id;

    public User(String pseudo, String idUser) {
        this.pseudo = pseudo;
        this.idUser = idUser;
        this.id = UUID.randomUUID();
    }

    // GETTERS
    public String getIdUser() {
        return this.idUser;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public UUID getId() {
        return id;
    }

    // SETTERS
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
