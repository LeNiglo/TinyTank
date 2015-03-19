package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.gameComponent.playerData.Player;

import java.util.List;

/**
 * Created by andres_k on 16/03/2015.
 */
public class RoundController {
    private List<Player> players;

    public RoundController(List<Player> players) {
        this.players = players;
    }
}
