package com.lefrantguillaume;

import com.lefrantguillaume.game.Game;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.master.Master;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main {
    public static void main(String args[]) {
        TinyServer server = new TinyServer(11111, 11222);
        server.start();
        Master master = new Master();
        master.initServer();
        new Game();
    }
}
