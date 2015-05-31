package com.lefrantguillaume.networkComponent.networkGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 11/03/2015.
 */

public class NetworkRegister {
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MessageModel.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageConnect.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageDisconnect.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageMove.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageShoot.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageSpell.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageChangeTeam.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePlayerDelete.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePlayerNew.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePlayerUpdateState.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePlayerUpdatePosition.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePlayerRevive.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageCollision.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessagePutObstacle.class);
        kryo.register(com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageGestRound.class);
        kryo.register(EnumTanks.class);
    }
}
