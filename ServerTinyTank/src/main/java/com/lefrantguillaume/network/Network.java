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

    static public String getIp() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return (ip);
        } catch (Exception e) {
            System.out.println("Unable to get your IP: " + e.getMessage() + ". Aborting");
            //System.exit(0);
        }
        return ("0.0.0.0");
    }

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MessageModel.class);
        kryo.register(MessageConnect.class);
        kryo.register(MessageDisconnect.class);
        kryo.register(MessageMove.class);
        kryo.register(MessageShoot.class);
    }

    static public class MessageModel {
        protected String pseudo;
        protected int id;

        public MessageModel() {}

        public String getPseudo() { return pseudo; }
        public int getId() { return id; }
        public void setPseudo(String pseudo) { this.pseudo = pseudo; }
        public void setId(int id) { this.id = id; }
    }

    static public class MessageMove extends MessageModel {
        private int direction;
        private boolean move;

        public MessageMove() {}

        public int getDirection() { return direction; }
        public boolean getMove() { return move; }

        public void setDirection(int direction) { this.direction = direction; }
        public void setMove(boolean move) { this.move = move; }
    }

    static public class MessageConnect extends MessageModel {
        private String password;
        private boolean success;

        public MessageConnect() {}

        public String getPassword() { return password; }
        public boolean isSuccess() { return success; }
        public void setPassword(String password) { this.password = password; }
        public void setSuccess(boolean success) { this.success = success; }
    }


    static public class MessageDisconnect extends MessageModel {
        private boolean success;

        public MessageDisconnect() {}

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    static public class MessageShoot extends MessageModel {
        private int valueKeyPressed;

        public MessageShoot() {}

        public int getValueKeyPressed() { return valueKeyPressed; }
        public void setValueKeyPressed(int valueKeyPressed) { this.valueKeyPressed = valueKeyPressed; }
    }
}