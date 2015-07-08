package com.lefrantguillaume.utils.configs;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CurrentUser {
    private static String pseudo;
    private static String id;
    private static String idTeam;
    private static boolean inGame;

    public CurrentUser(String pseudo, String id, String idTeam) {
        this.pseudo = pseudo;
        this.id = id;
        this.idTeam = idTeam;
        this.inGame = false;
    }

    // GETTERS
    public static String getId() {
        return id;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static boolean isInGame() {
        return inGame;
    }

    public static String getIdTeam() {
        return idTeam;
    }

    // SETTERS
    public static void setPseudo(String pseudo) {
        CurrentUser.pseudo = pseudo;
    }

    public static void setId(String id) {
        CurrentUser.id = id;
    }

    public static void setInGame(boolean inGame) {
        CurrentUser.inGame = inGame;
    }

    public static void setIdTeam(String idTeam) {
        CurrentUser.idTeam = idTeam;
    }
}
