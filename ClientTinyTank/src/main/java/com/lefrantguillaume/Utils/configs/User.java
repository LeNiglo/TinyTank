package com.lefrantguillaume.Utils.configs;

/**
 * Created by andres_k on 13/03/2015.
 */
public class User {
    private static String pseudo;
    private static int id;

    public User(String pseudo, int id){
        this.pseudo = pseudo;
        this.id = id;
    }
    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static void setPseudo(String pseudo) {
        User.pseudo = pseudo;
    }
}
