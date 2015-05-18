package com.lefrantguillaume.networkComponent.gameServer;

import com.esotericsoftware.kryonet.Connection;
import com.lefrantguillaume.networkComponent.gameServer.clientmsgs.MessageModel;

/**
 * Created by andres_k on 18/05/2015.
 */
public class RequestFactory {
    public static Request createRequest(MessageModel message){
        return new Request(null, message);
    }

    public static Request createRequest(Connection connection, MessageModel message){
        return new Request(connection, message);
    }
}
