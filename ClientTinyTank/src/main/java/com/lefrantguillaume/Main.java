package com.lefrantguillaume;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.master.MasterGame;
import org.codehaus.jettison.json.JSONException;
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
            MasterGame game = new MasterGame();
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
