package com.lefrantguillaume.Utils.configs;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CurrentUser {
    private static String pseudo;
    private static String id;

    //TODO mettre l'id en String
    public CurrentUser(String pseudo, String id){
        this.pseudo = pseudo;
        this.id = id;
    }
    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        CurrentUser.id = id;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static void setPseudo(String pseudo) {
        CurrentUser.pseudo = pseudo;
    }
}
