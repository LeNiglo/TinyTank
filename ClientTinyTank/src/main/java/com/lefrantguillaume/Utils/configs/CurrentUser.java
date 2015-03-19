package com.lefrantguillaume.Utils.configs;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CurrentUser {
    private static String pseudo;
    private static int id;

    public CurrentUser(String pseudo, int id){
        this.pseudo = pseudo;
        this.id = id;
    }
    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        CurrentUser.id = id;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static void setPseudo(String pseudo) {
        CurrentUser.pseudo = pseudo;
    }
}
