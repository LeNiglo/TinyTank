package com.lefrantguillaume.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by Styve on 10/03/2015.
 */

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public final int port = 13333;

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