package com.lefrantguillaume.graphicsComponent.graphics.controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Created by Styve on 27/05/2015.
 */
public class LoginController implements ScreenController, KeyInputHandler {
    private Nifty nifty;
    private Screen screen;
    private Element loginInput;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // on est bind dans le xml. le screen id="start" est relié à ce controlleur
        System.out.println("LoginController binded");
        this.nifty = nifty;
        this.screen = screen;
        this.loginInput = screen.findElementByName("login");
    }

    @Override
    public void onStartScreen() {
        System.out.println("LoginController started");
    }

    @Override
    public void onEndScreen() {
        System.out.println("LoginController ended");
    }

    @Override
    public boolean keyEvent(NiftyInputEvent e) {
        return false;
    }

    @NiftyEventSubscriber(id="ConnectButton")
    public void onClick(String id, NiftyMousePrimaryClickedEvent event) {
        System.out.println("element with id [" + id + "] clicked at [" + event.getMouseX() + ", " + event.getMouseY() + "]"); }

    public void connect() {
        System.out.println("Cliqué sur Connect !");
    }
}
