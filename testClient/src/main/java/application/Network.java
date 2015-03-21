package application;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.List;

/**
 * Created by Styve on 10/03/2015.
 * lol
 */

// This class is a convenient place to keep things common to both the client and server.
public class Network {

    public enum EnumTanks {
        NULL(-1), TIGER(0), SNIPER(1), RUSHER(2);

        private final int id;

        EnumTanks(int id) { this.id = id;}

        public int getId() {return id;}
    }

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
        kryo.register(MessageDelete.class);
        kryo.register(MessagePlayerNew.class);
        kryo.register(MessagePlayerUpdate.class);

        kryo.register(MessageHasMap.class);
        kryo.register(MessageDownloadAnswer.class);
    }

    static public class MessageModel {
        protected String pseudo;
        protected String id;

        public MessageModel() {}

        public String getPseudo() { return pseudo; }
        public String getId() { return id; }
        public void setPseudo(String pseudo) { this.pseudo = pseudo; }
        public void setId(String id) { this.id = id; }
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
        private String mapName;
        private String encodedMap;
        private List<String> users;

        MessageConnect() {}
        MessageConnect(String mapName, String encodedMap) { this.mapName = mapName; this.encodedMap = encodedMap;}

        public String getMapName() {return mapName;}
        public String getEncodedMap() {return encodedMap;}
        public List<String> getUsers() {return users;}

        public void setMapName(String mapName) {this.mapName = mapName;}
        public void setEncodedMap(String encodedMap) {this.encodedMap = encodedMap;}
        public void setUsers(List<String> users) {this.users = users;}
    }


    static public class MessageDisconnect extends MessageModel {
        private boolean success;

        public MessageDisconnect() {}

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    static public class MessageShoot extends MessageModel {
        private int valueKeyPressed;
        private float angle;

        public MessageShoot() {}

        public int getValueKeyPressed() { return valueKeyPressed; }
        public float getAngle() {return angle;}

        public void setValueKeyPressed(int valueKeyPressed) { this.valueKeyPressed = valueKeyPressed; }
        public void setAngle(float angle) {this.angle = angle;}
    }

    static public class MessageSpell extends MessageModel {
        private int valueKeyPressed;
        private float angle;
        private float x;
        private float y;

        public MessageSpell() {}

        public int getValueKeyPressed() { return valueKeyPressed; }
        public float getAngle() {return angle;}
        public float getX() {return x;}

        public void setValueKeyPressed(int valueKeyPressed) { this.valueKeyPressed = valueKeyPressed; }
        public void setAngle(float angle) {this.angle = angle;}
        public void setX(float x) {this.x = x;}

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    static public class MessagePlayerNew extends MessageModel {
        private EnumTanks enumTanks;

        public EnumTanks getEnumTanks() {return enumTanks;}
        public void setEnumTanks(EnumTanks enumTanks) {this.enumTanks = enumTanks;}
    }

    static public class MessageHasMap extends MessageModel {
        private boolean value;

        public MessageHasMap() {}
        public MessageHasMap(boolean value) { this.value = value; }

        public boolean isValue() { return value; }
        public void setValue(boolean value) { this.value = value; }
    }

    static public class MessageDownloadAnswer {
        private String fileName;
        private int fileSize;

        public MessageDownloadAnswer() {}
        public MessageDownloadAnswer(String fileName, int fileSize) { this.fileName = fileName; this.fileSize = fileSize; }

        public String getFileName() {return fileName;}
        public int getFileSize() {return fileSize;}

        public void setFileName(String fileName) {this.fileName = fileName;}
        public void setFileSize(int fileSize) {this.fileSize = fileSize;}
    }

    static class MessageChangeTeam extends MessageModel {
        public MessageChangeTeam() {}
    }

    static class MessagePlayerUpdate extends MessageModel {
        private Player player;

        public MessagePlayerUpdate() {}

        public Player getPlayer() {return player;}

        public void setPlayer(Player player) {this.player = player;}
    }

    static class MessageDelete extends MessageModel {
        public MessageDelete() {}
    }
}