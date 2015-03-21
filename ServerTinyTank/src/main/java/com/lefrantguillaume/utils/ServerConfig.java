package com.lefrantguillaume.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by Styve on 12/03/2015.
 */
public class ServerConfig {
    public static String gameName;
    public static int tcpPort;
    public static int udpPort;
    public static int maxAllowedPlayers;
    public static int maxAllowedPing;
    public static boolean friendlyFire;
    public static boolean allyNoBlock;

    public ServerConfig() {
    }

    public static boolean loadConfig() {
        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            gameName = props.getProperty("gameName", "My First TinyTank Game");
            tcpPort = Integer.valueOf(props.getProperty("tcpPort", "13333"));
            udpPort = Integer.valueOf(props.getProperty("udpPort", "13444"));
            maxAllowedPlayers = Integer.valueOf(props.getProperty("maxAllowedPlayers", "8"));
            maxAllowedPing = Integer.valueOf(props.getProperty("maxAllowedPing", "100"));
            friendlyFire = Boolean.valueOf(props.getProperty("friendlyFire", "false"));
            allyNoBlock = Boolean.valueOf(props.getProperty("allyNoBlock", "true"));

            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Config file does not exist");
            return (false);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            return (false);
        }
        return (true);
    }

    public static boolean writeConfig() {
        File configFile = new File("config.properties");
        try {
            Properties props = new Properties();

            props.setProperty("maxAllowedPing", "100");
            props.setProperty("maxAllowedPlayers", "8");
            props.setProperty("udpPort", "13444");
            props.setProperty("tcpPort", "13333");
            props.setProperty("gameName", "My TinyTank Game");
            props.getProperty("friendlyFire", "0");
            props.getProperty("allyNoBlock", "1");
            init();
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, " This file is the Host settings.");
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Config file does not exist");
            return (false);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            return (false);
        }
        return (true);
    }

    private static void init() {
        gameName = "My First TinyTank Game";
        tcpPort = 13333;
        udpPort = 13444;
        maxAllowedPlayers = 8;
        maxAllowedPing = 100;
        friendlyFire = false;
        allyNoBlock = true;
    }
}
