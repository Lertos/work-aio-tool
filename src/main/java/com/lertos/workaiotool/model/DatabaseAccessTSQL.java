package com.lertos.workaiotool.model;

public class DatabaseAccessTSQL extends DatabaseAccess {

    public DatabaseAccessTSQL(ItemSQL itemSQL, String procedureName) {
        super(itemSQL, procedureName, "com.microsoft.sqlserver.jdbc.SQLServerDriver", "definition");
    }

    //TransactSQL: "jdbc:sqlserver://<host>:<port>;database=<database>;user=<username>;password=<password>;encrypt=true;"
    @Override
    protected String buildConnectionString(String databaseName) {
        if (!isValidConnectionString())
            return null;

        StringBuilder sb = new StringBuilder();

        sb.append("jdbc:sqlserver://");
        sb.append(itemSQL.getHost());

        if (itemSQL.getPort() > -2) {
            sb.append(":");
            sb.append(itemSQL.getPort());
        }

        sb.append(";database=");
        sb.append(databaseName);
        sb.append(";user=");
        sb.append(itemSQL.getUsername());
        sb.append(";password=");
        sb.append(itemSQL.getPassword());
        sb.append(";encrypt=true;");

        return sb.toString();
    }

    //TransactSQL: SELECT OBJECT_DEFINITION (OBJECT_ID(N'<routine_name>'))
    @Override
    protected String buildStatementString(String databaseName) {
        return "SELECT OBJECT_DEFINITION (OBJECT_ID(N'" + procedureName + "')) AS definition";
    }

}
