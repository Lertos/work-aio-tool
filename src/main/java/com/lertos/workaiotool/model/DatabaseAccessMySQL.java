package com.lertos.workaiotool.model;

public class DatabaseAccessMySQL extends DatabaseAccess {

    public DatabaseAccessMySQL(ItemSQL itemSQL, String procedureName) {
        super(itemSQL, procedureName, "com.mysql.cj.jdbc.Driver", "routine_definition");
    }

    //MYSQL: "jdbc:mysql://<host>/<database>?user=<username>&password=<password>";
    @Override
    protected String buildConnectionString(String databaseName) {
        if (!isValidConnectionString())
            return null;

        StringBuilder sb = new StringBuilder();

        sb.append("jdbc:mysql://");
        sb.append(itemSQL.getHost());

        if (itemSQL.getPort() > -2) {
            sb.append(":");
            sb.append(itemSQL.getPort());
        }

        sb.append("/");
        sb.append(databaseName);
        sb.append("?");
        sb.append("user=");
        sb.append(itemSQL.getUsername());
        sb.append("&password=");
        sb.append(itemSQL.getPassword());

        return sb.toString();
    }

    //MYSQL: SELECT routine_definition FROM INFORMATION_SCHEMA.ROUTINES WHERE specific_name = '<routine_name>' and routine_schema = '<database_name>'
    @Override
    protected String buildStatementString(String databaseName) {
        return "SELECT routine_definition FROM INFORMATION_SCHEMA.ROUTINES WHERE specific_name = '" + procedureName + "' and routine_schema = '" + databaseName + "'";
    }

}
