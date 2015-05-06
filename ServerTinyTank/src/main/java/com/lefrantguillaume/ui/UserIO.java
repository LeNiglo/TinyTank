package com.lefrantguillaume.ui;

import java.io.Console;

/**
 * Created by leniglo on 06/05/15.
 */
public class UserIO {

    private Console c = null;

    UserIO() {

        this.c = System.console();
        if (this.c == null) {
            System.err.println("No console.");
            System.exit(1);
        }

    }
}
