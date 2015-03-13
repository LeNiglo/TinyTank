package com.lefrantguillaume.networkComponent;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.MessageConnect;
import com.lefrantguillaume.networkComponent.messages.msg.MessageDisconnect;
import com.lefrantguillaume.networkComponent.messages.msg.MessageMove;
import com.lefrantguillaume.networkComponent.messages.msg.MessageShoot;

/**
 * Created by andres_k on 11/03/2015.
 */

public class NetworkRegister {
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MessageModel.class);
        kryo.register(MessageConnect.class);
        kryo.register(MessageDisconnect.class);
        kryo.register(MessageMove.class);
        kryo.register(MessageShoot.class);
    }
}
