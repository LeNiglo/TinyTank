package com.lefrantguillaume.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Styve on 10/03/2015.
 */

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public int tcpPort = 13333;
    static public int udpPort = 13444;

    static public String getIp() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return (ip);
        } catch (Exception e) {
            System.out.println("Unable to get your IP: " + e.getMessage() + ". Aborting");
            System.exit(0);
        }
        return ("0.0.0.0");
    }

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
    }

    static public class SomeRequest {
        public String text;
    }

    static public class SomeResponse {
        public String text;
    }
}