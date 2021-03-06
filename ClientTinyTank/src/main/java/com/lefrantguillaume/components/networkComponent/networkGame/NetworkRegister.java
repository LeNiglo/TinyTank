package com.lefrantguillaume.components.networkComponent.networkGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.lefrantguillaume.components.collisionComponent.EnumCollision;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.*;

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
        kryo.register(MessagePlayerObserverDelete.class);
        kryo.register(MessagePlayerObserverNew.class);
        kryo.register(MessagePlayerUpdateState.class);
        kryo.register(MessagePlayerUpdatePosition.class);
        kryo.register(MessagePlayerRevive.class);
        kryo.register(MessageCollision.class);
        kryo.register(MessagePutObstacle.class);
        kryo.register(MessageObstacleUpdateState.class);
        kryo.register(MessageShotUpdateState.class);
        kryo.register(MessageChat.class);
        kryo.register(MessageRoundKill.class);
        kryo.register(MessageRoundScore.class);
        kryo.register(MessageRoundStart.class);
        kryo.register(MessageRoundEnd.class);
        kryo.register(MessageDeleteObject.class);

        kryo.register(EnumGameObject.class);
        kryo.register(EnumCollision.class);
    }
}
