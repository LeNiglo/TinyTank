package com.lefrantguillaume.graphicsComponent.graphics.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.graphicsComponent.graphics.WindowLogin;
import com.lefrantguillaume.networkComponent.networkData.DataServer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by Styve on 31/05/2015.
 */
public class LoginController implements ScreenController {
    TextField loginField = null;
    TextField passField = null;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // on est bind dans le xml. le screen id="start" est relié à ce controlleur
        System.out.println("LoginController binded");
        loginField = screen.findNiftyControl("login", TextField.class);
        passField = screen.findNiftyControl("pass", TextField.class);
    }

    @Override
    public void onStartScreen() {
        System.out.println("LoginController started");
    }

    @Override
    public void onEndScreen() {
        System.out.println("LoginController ended");
    }

    public void connect() {
        System.out.println("Cliqué sur Connect !");
        try {
            System.out.println("user: " + loginField.getDisplayedText() + " // pass: " + passField.getRealText());
            Pair<Boolean, String> p = DataServer.authentification(loginField.getDisplayedText(), passField.getRealText());
            if (p.getV1()) {
                JSONObject object = new JSONObject(p.getV2());
                Debug.debug("my id = " + object.get("_id"));
                CurrentUser.setId(object.get("_id").toString());
                CurrentUser.setPseudo(object.get("username").toString());
                //stateGame.enterState(EnumWindow.ACCOUNT.getValue());
            } else {
                // TODO Display message on client.
                Debug.debug("Error Login : " + p.getV2());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
