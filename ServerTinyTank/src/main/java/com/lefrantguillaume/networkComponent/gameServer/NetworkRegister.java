package com.lefrantguillaume.networkComponent.gameServer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.lefrantguillaume.gameComponent.gameobjects.EnumObjects;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.types.EnumTanks;
import com.lefrantguillaume.networkComponent.gameServer.clientmsgs.*;

import java.util.ArrayList;

/**
 * Created by Styve on 10/03/2015.
 * lol
 */

// This class is a convenient place to keep things common to both the client and server.
public class NetworkRegister {

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MessageModel.class);
        kryo.register(MessageConnect.class);
        kryo.register(MessageDisconnect.class);
        kryo.register(MessageMove.class);
        kryo.register(MessageShoot.class);
        kryo.register(MessageSpell.class);
        kryo.register(MessageChangeTeam.class);
        kryo.register(MessagePlayerDelete.class);
        kryo.register(MessagePlayerNew.class);
        kryo.register(MessagePlayerUpdateState.class);
        kryo.register(MessagePlayerUpdatePosition.class);
        kryo.register(MessagePlayerRevive.class);
        kryo.register(MessageCollision.class);
        kryo.register(MessagePutObstacle.class);
        kryo.register(EnumTanks.class);
        kryo.register(EnumObjects.class);
        kryo.register(MessageNeedMap.class);
        kryo.register(MessageDownload.class);
        kryo.register(ArrayList.class);
    }
}