package com.lertos.workaiotool.model;

import com.lertos.workaiotool.Helper;

import java.util.ArrayList;

public class DatabaseAccessMySQL extends DatabaseAccess {

    public DatabaseAccessMySQL(ItemSQL itemSQL, String procedureName) {
        super(itemSQL, procedureName, "com.mysql.cj.jdbc.Driver", "routine_definition");
    }

    //MYSQL: "jdbc:mysql://<host>/<database>?user=<username>&password=<password>";
    @Override
    protected ArrayList<String> buildConnectionStrings() {
        ArrayList<String> connectionStrings = new ArrayList<>();

        if (isValidConnectionString()) {
            StringBuilder sb = new StringBuilder();

            connectionStrings.add(sb.toString());
        }
        return connectionStrings;
    }

    @Override
    protected String buildStatementString() {
        return "SELECT routine_definition FROM INFORMATION_SCHEMA.ROUTINES WHERE specific_name = '" + procedureName + "'";
    }

    private boolean isValidConnectionString() {
        if (itemSQL.getHost() == null || itemSQL.getHost().isEmpty()) {
            Helper.showAlert("The value given for 'host' is empty or cannot be found");
            return false;
        } else if (itemSQL.getUsername() == null || itemSQL.getUsername().isEmpty()) {
            Helper.showAlert("The value given for 'username' is empty or cannot be found");
            return false;
        } else if (itemSQL.getPassword() == null || itemSQL.getPassword().isEmpty()) {
            Helper.showAlert("The value given for 'password' is empty or cannot be found");
            return false;
        }
        return true;
    }

}
