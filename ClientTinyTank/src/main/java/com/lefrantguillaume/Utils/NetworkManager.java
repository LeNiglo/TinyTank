package com.lefrantguillaume.Utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by andres_k on 11/03/2015.
 */
public class NetworkManager {
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
