package com.lefrantguillaume;

import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.ui.Console;
import com.lefrantguillaume.ui.ServerGUI;
import com.lefrantguillaume.utils.ServerConfig;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main {

    private ServerGUI gui = null;
    private Console csl = null;

    public static void main(String args[]) {
        if (!ServerConfig.loadConfig())
            ServerConfig.writeConfig();

        ServerGUI app = new ServerGUI();
        WindowObserver a = new WindowObserver(app);
        new WindowController(a);
    }
}
