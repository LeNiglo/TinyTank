package com.lefrantguillaume.gameComponent.playerData;

/**
 * Created by andres_k on 17/03/2015.
 */
public class User {
    private String pseudo;
    private int id;

    public User(String pseudo, int id) {
        this.pseudo = pseudo;
        this.id = id;
    }

    // GETTERS
    public int getId() {
        return this.id;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    // SETTERS
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setId(int id) {
        this.id = id;
    }
}
