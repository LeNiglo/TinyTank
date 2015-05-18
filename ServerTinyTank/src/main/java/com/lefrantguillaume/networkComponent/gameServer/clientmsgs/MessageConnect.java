package com.lefrantguillaume.networkComponent.gameServer.clientmsgs;

import java.util.List;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageConnect extends MessageModel {
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