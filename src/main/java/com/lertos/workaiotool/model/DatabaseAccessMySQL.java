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

    private boolean isValidConnectionString() {
        if (itemSQL.getHost() == null || itemSQL.getHost().isEmpty()) {
            Helper.showAlert("The value given for 'host' is empty or cannot be found");
            return false;
        } else if (itemSQL.getPort() < 0) {
            Helper.showAlert("The value given for 'port' is incorrect");
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
