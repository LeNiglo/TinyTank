package com.lefrantguillaume;

import com.lefrantguillaume.gestGame.Utils.configs.CurrentUser;
import com.lefrantguillaume.gestGame.Utils.configs.WindowConfig;
import com.lefrantguillaume.gestAccount.MasterLauncher;
import org.newdawn.slick.SlickException;

/**
 * Created by andres_k on 10/03/2015.
 */
public class Main {
    private static WindowConfig windowConfig;
    private static CurrentUser user;

    public static void main(String args[]) {

        windowConfig = new WindowConfig();
        try {
            MasterLauncher launcher = new MasterLauncher();
            launcher.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
