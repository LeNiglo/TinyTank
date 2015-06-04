package com.lefrantguillaume.components.networkComponent;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by leniglo on 30/05/15.
 */
public class ServerEntry {

    private String name;
    private String ip;
    private Integer udpPort;
    private Integer tcpPort;
    private List<String> users = new ArrayList<>();
    private String map;
    private Date started_at;
    private Date last_active;

    public ServerEntry(String name, String ip, JSONObject ports, JSONArray users, String map, String started_at, String last_active) throws JSONException, ParseException {
        this.name = name;
        this.ip = ip;
        this.udpPort = ports.getInt("udp");
        this.tcpPort = ports.getInt("tcp");
        int len = users.length();
        for (int i = 0; i < len; i++) {
            this.users.add(users.get(i).toString());
        }
        this.map = map;
        this.started_at = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(started_at);
        this.last_active = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(last_active);
    }

    public ServerEntry(String name, String ip, String map, int udp, int tcp) {
        this.name = name;
        this.ip = ip;
        this.map = map;
        this.udpPort = udp;
        this.tcpPort = tcp;
        this.started_at = new Date();
        this.last_active = new Date();
    }

    public String getName() {
        return this.name;
    }

    public String getIp() {
        return this.ip;
    }

    public Integer getUdpPort() {
        return this.udpPort;
    }

    public Integer getTcpPort() {
        return this.tcpPort;
    }

    public List<String> getUsers() {
        return this.users;
    }

    public String getMap() {
        return this.map;
    }

    public Date getStarted_at() {
        return this.started_at;
    }

    public Date getLast_active() {
        return this.last_active;
    }

    @Override
    public String toString() {
        return "Server '"+this.name+"' ["+this.ip+"], "+this.users.size()+" user(s).";
    }
}
