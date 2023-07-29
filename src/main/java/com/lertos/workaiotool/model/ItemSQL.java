package com.lertos.workaiotool.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemSQL implements Serializable {

    private String tabName;
    private String host;
    private int port;
    private String username;
    private String password;
    private ArrayList<String> databaseNames;

    public ItemSQL(String tabName, String host, int port, String username, String password, ArrayList<String> databaseNames) {
        this.tabName = tabName;
        this.host = host;
        this.port = port;
        this.databaseNames = databaseNames;
        this.username = username;
        this.password = password;
    }

    public String getTabName() {
        return tabName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ArrayList<String> getDatabaseNames() {
        return databaseNames;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
