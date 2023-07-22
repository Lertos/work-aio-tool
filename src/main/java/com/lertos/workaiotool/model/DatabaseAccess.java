package com.lertos.workaiotool.model;

import com.lertos.workaiotool.Helper;

import java.sql.*;

public abstract class DatabaseAccess {

    protected ItemSQL itemSQL;
    protected String procedureName;
    protected String driverString;
    protected String connectionString;
    protected String statementString;
    protected String returnedColumnName;
    protected Connection connect = null;
    protected Statement statement = null;
    protected ResultSet resultSet = null;

    public DatabaseAccess(ItemSQL itemSQL, String procedureName, String driverString, String returnedColumnName) {
        this.itemSQL = itemSQL;
        this.procedureName = procedureName;
        this.driverString = driverString;
        this.returnedColumnName = returnedColumnName;
        this.connectionString = buildConnectionString();
        this.statementString = buildStatementString();
    }

    protected abstract String buildConnectionString();

    protected abstract String buildStatementString();

    public String getProcedureDefinition() {
        try {
            //This will load the driver; each SQL type has its own driver
            Class.forName(driverString);

            connect = DriverManager.getConnection(connectionString);
            statement = connect.createStatement();

            resultSet = statement.executeQuery(statementString);

            return getResult(resultSet);
        } catch (SQLException sqlException) {
            Helper.showAlert(sqlException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }

    private String getResult(ResultSet resultSet) throws SQLException {
        String procedure = null;

        while (resultSet.next()) {
            procedure = resultSet.getString(returnedColumnName);

            if (procedure.equalsIgnoreCase("null")) {
                Helper.showAlert("Either the procedure does not exist, or you don't have permission to view the definition");
                return null;
            }
        }
        return procedure;
    }

    private void close() {
        try {
            if (resultSet != null)
                resultSet.close();

            if (statement != null)
                statement.close();

            if (connect != null)
                connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
