package com.lefrantguillaume;

import com.lefrantguillaume.ui.UserIO;
import com.lefrantguillaume.ui.ServerGUI;
import com.lefrantguillaume.utils.ServerConfig;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main {

    private ServerGUI gui = null;
    private UserIO uio = null;

    public static void main(String args[]) {
        if (!ServerConfig.loadConfig())
            ServerConfig.writeConfig();

        ServerGUI app = new ServerGUI();
        WindowObserver a = new WindowObserver(app);
        new WindowController(a);
    }
}
