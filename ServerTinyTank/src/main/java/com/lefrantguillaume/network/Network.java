package com.lefrantguillaume.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.lefrantguillaume.game.EnumTanks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Styve on 10/03/2015.
 * lol
 */

// This class is a convenient place to keep things common to both the client and server.
public class Network {

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
        kryo.register(MessagePlayerUpdateState.class);
        kryo.register(MessagePlayerUpdatePosition.class);
        kryo.register(EnumTanks.class);
        kryo.register(MessageNeedMap.class);
        kryo.register(MessageDownload.class);
        kryo.register(MessageGameObjects.class);
        kryo.register(ArrayList.class);
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
        private String mapFileName;
        private String encodedMap;
        private String encodedJson;
        private List<String> users;

        public MessageConnect() {}
        public MessageConnect(String mapName, String mapFileName, String encodedMap, String encodedJson, List<String> users) {
            this.mapName = mapName;
            this.mapFileName = mapFileName;
            this.encodedMap = encodedMap;
            this.encodedJson = encodedJson;
            this.users = users;
        }

        public String getMapName() {return mapName;}
        public String getEncodedMap() {return encodedMap;}
        public List<String> getUsers() {return users;}
        public String getEncodedJson() {return encodedJson;}
        public String getMapFileName() {return mapFileName;}
        public void setMapName(String mapName) {this.mapName = mapName;}
        public void setEncodedMap(String encodedMap) {this.encodedMap = encodedMap;}
        public void setUsers(List<String> users) {this.users = users;}
        public void setEncodedJson(String encodedJson) {this.encodedJson = encodedJson;}
        public void setMapFileName(String mapFileName) {this.mapFileName = mapFileName;}
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
        public float getY() {return y;}
        public void setValueKeyPressed(int valueKeyPressed) { this.valueKeyPressed = valueKeyPressed; }
        public void setAngle(float angle) {this.angle = angle;}
        public void setX(float x) {this.x = x;}
        public void setY(float y) {this.y = y;}
    }

    static public class MessagePlayerNew extends MessageModel {
        private EnumTanks enumTanks;

        public MessagePlayerNew() {}

        public EnumTanks getEnumTanks() {return enumTanks;}
        public void setEnumTanks(EnumTanks enumTanks) {this.enumTanks = enumTanks;}
    }

    static public class MessageChangeTeam extends MessageModel {
        public MessageChangeTeam() {}
    }

    static public class MessageDelete extends MessageModel {
        public MessageDelete() {}
    }

    static public class MessagePlayerUpdateState extends MessageModel {
        private float currentLfe;
        private float armor;
        private float shieldEffect;
        private float slowEffect;
        private float boostEffect;

        public MessagePlayerUpdateState() {}

        public float getBoostEffect() {return boostEffect;}
        public float getSlowEffect() {return slowEffect;}
        public float getShieldEffect() {return shieldEffect;}
        public float getArmor() {return armor;}
        public float getCurrentLfe() {return currentLfe;}
        public void setBoostEffect(float boostEffect) {this.boostEffect = boostEffect;}
        public void setSlowEffect(float slowEffect) {this.slowEffect = slowEffect;}
        public void setShieldEffect(float shieldEffect) {this.shieldEffect = shieldEffect;}
        public void setArmor(float armor) {this.armor = armor;}
        public void setCurrentLfe(float currentLfe) {this.currentLfe = currentLfe;}
    }

    static public class MessagePlayerUpdatePosition extends MessageModel {
        private float x;
        private float y;

        public MessagePlayerUpdatePosition() {}

        public float getX() {return x;}
        public float getY() {return y;}
        public void setX(float x) {this.x = x;}
        public void setY(float y) {this.y = y;}
    }

    static public class MessageNeedMap {
        private boolean value;

        public MessageNeedMap() {}
        public MessageNeedMap(boolean value) { this.value = value; }

        public boolean isValue() { return value; }
        public void setValue(boolean value) { this.value = value; }
    }

    static public class MessageGameObjects {
        public MessageGameObjects() {}
    }

    static public class MessageDownload {
        private String fileName;
        private long fileSize;

        public MessageDownload() {}
        public MessageDownload(String fileName, long fileSize) { this.fileName = fileName; this.fileSize = fileSize; }

        public String getFileName() {return fileName;}
        public long getFileSize() {return fileSize;}
        public void setFileName(String fileName) {this.fileName = fileName;}
        public void setFileSize(long fileSize) {this.fileSize = fileSize;}
    }
}