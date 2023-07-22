package com.lertos.workaiotool.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemSQL implements Serializable {

    private String host;
    private int port;
    private String username;
    private String password;
    private ArrayList<String> databaseNames;

    public ItemSQL(String host, int port, String username, String password, ArrayList<String> databaseNames) {
        this.host = host;
        this.port = port;
        this.databaseNames = databaseNames;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<String> getDatabaseName() {
        return databaseNames;
    }

    public void setDatabaseName(ArrayList<String> databaseNames) {
        this.databaseNames = databaseNames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
