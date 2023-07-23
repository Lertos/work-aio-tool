package com.lertos.workaiotool.model;

import com.lertos.workaiotool.Helper;

import java.sql.*;
import java.util.ArrayList;

public abstract class DatabaseAccess {

    protected ItemSQL itemSQL;
    protected String procedureName;
    protected String driverString;
    protected ArrayList<String> connectionStringList;
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
        this.connectionStringList = buildConnectionStrings();
        this.statementString = buildStatementString();
    }

    protected abstract ArrayList<String> buildConnectionStrings();

    protected abstract String buildStatementString();

    public String getProcedureDefinition() {
        try {
            //This will load the driver; each SQL type has its own driver
            Class.forName(driverString);

            //TODO: Loop through all of the connection strings
            connect = DriverManager.getConnection(connectionStringList.get(0));
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
