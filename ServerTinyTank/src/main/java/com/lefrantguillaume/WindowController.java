package com.lefrantguillaume;

import java.util.Observer;

public class WindowController {
    private static WindowControllerObserver messages;

    WindowController(Observer o) {
        messages = new WindowControllerObserver();
        messages.addObserver(o);
    }

    public static void addConsoleMsg(String msg) {
        WindowController.messages.addMessage(msg);
    }
}
