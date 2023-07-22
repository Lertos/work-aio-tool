package com.lertos.workaiotool.model;

public class DatabaseAccessMySQL extends DatabaseAccess {

    public DatabaseAccessMySQL(ItemSQL itemSQL, String procedureName) {
        super(itemSQL, procedureName, "com.mysql.cj.jdbc.Driver", "routine_definition");
    }

    @Override
    protected String buildConnectionString() {
        return "";
        //"jdbc:mysql://<host>/<database>?user=<username>&password=<password>";
    }

    @Override
    protected String buildStatementString() {
        return "SELECT routine_definition FROM INFORMATION_SCHEMA.ROUTINES WHERE specific_name = '" + procedureName + "'";
    }

}
