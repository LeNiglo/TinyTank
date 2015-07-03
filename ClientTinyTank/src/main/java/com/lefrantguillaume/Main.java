package com.lefrantguillaume;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.master.MasterGame;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.SlickException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by andres_k on 10/03/2015.
 */
public class Main {
    private static CurrentUser user;

    public static void main(String args[]) {

        WindowConfig windowConfig = new WindowConfig();
        try {
            MasterGame game = new MasterGame();
            Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
            Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
