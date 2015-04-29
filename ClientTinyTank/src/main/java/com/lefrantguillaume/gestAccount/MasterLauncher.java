package com.lefrantguillaume.gestAccount;

import com.lefrantguillaume.gestGame.master.MasterGame;
import org.newdawn.slick.SlickException;

/**
 * Created by andres_k on 29/04/2015.
 */
public class MasterLauncher {

    public void start() throws SlickException {
        MasterGame game = new MasterGame();
        game.start();

    }
}
