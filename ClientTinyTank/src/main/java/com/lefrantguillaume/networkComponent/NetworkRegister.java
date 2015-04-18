package com.lefrantguillaume.networkComponent;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.*;

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
        kryo.register(MessageSpell.class);
        kryo.register(MessageChangeTeam.class);
        kryo.register(MessagePlayerDelete.class);
        kryo.register(MessagePlayerNew.class);
        kryo.register(MessagePlayerUpdateState.class);
        kryo.register(MessagePlayerUpdatePosition.class);
        kryo.register(MessageCollision.class);
        kryo.register(EnumTanks.class);
    }
}
