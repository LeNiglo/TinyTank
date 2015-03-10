package com.lefrantguillaume;

import com.lefrantguillaume.game.Game;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.web.Web;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main {
    public static void main(String args[]) {
        TinyServer server = new TinyServer(11111, 11222);
        server.start();
        new Web();
        new Game();
    }
}
