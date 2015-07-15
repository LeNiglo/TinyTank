package com.lefrantguillaume.utils.tools;

import com.lefrantguillaume.utils.configs.GlobalVariable;

/**
 * Created by andres_k on 13/03/2015.
 */
public class ConsoleWriter {
    public static void debug(String message) {
        if (GlobalVariable.debug) {
            System.out.println(message);
        }
    }

    public static void write(String message) {
        System.out.println(message);
    }

    public static void err(String message) {
        System.err.println(message);
    }
}
