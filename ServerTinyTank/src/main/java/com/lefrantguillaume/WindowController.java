package com.lefrantguillaume;

import java.util.Observer;

public class WindowController {
    private static WindowControllerObserver messages;

    public WindowController(Observer o) {
        messages = new WindowControllerObserver();
        messages.addObserver(o);
    }

    public static void addConsoleMsg(String msg) {
        WindowController.messages.addInfo(msg);
    }

    public static void addConsoleErr(String msg) {
        WindowController.messages.addError(msg);
    }
}
