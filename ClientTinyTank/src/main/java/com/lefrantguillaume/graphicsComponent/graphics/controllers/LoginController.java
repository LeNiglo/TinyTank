package com.lefrantguillaume.graphicsComponent.graphics.controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Created by Styve on 27/05/2015.
 */
public class LoginController implements ScreenController{
    @Override
    public void bind(Nifty nifty, Screen screen) {
        // on est bind dans le xml. le screen id="start" est relié à ce controlleur
        System.out.println("LoginController binded");
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
    }
}
