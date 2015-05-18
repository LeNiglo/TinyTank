package com.lefrantguillaume.Utils.configs;

/**
 * Created by andres_k on 11/03/2015.
 */
public class MasterConfig {
    private static final int masterUdpPort = 13444;
    private static final int masterTcpPort = 13333;
    private static final String masterAddress = "127.0.0.1";
    private static final String mapConfigFile = "mapConfig";

    public MasterConfig() {
    }

    public static int getMasterUdpPort() {
        return masterUdpPort;
    }

    public static int getMasterTcpPort() {
        return masterTcpPort;
    }

    public static String getMasterAddress() {
        return masterAddress;
    }

    public static String getMapConfigFile() {
        return mapConfigFile;
    }
}
