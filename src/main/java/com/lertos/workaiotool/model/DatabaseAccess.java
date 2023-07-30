package com.lertos.workaiotool.model;

import com.lertos.workaiotool.Helper;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public abstract class DatabaseAccess {

    protected ItemSQL itemSQL;
    protected String procedureName;
    protected String driverString;
    protected String returnedColumnName;
    protected Connection connect = null;
    protected Statement statement = null;
    protected ResultSet resultSet = null;

    public DatabaseAccess(ItemSQL itemSQL, String procedureName, String driverString, String returnedColumnName) {
        this.itemSQL = itemSQL;
        this.procedureName = procedureName;
        this.driverString = driverString;
        this.returnedColumnName = returnedColumnName;
    }

    protected abstract String buildConnectionString(String databaseName);

    protected abstract String buildStatementString(String databaseName);

    //Returns NULL if definitions do not match, or the definition string if they all match
    public Pair<ReturnType, String> getProcedureDefinition() {
        //This will load the driver; each SQL type has its own driver
        try {
            Class.forName(driverString);
        } catch (Exception e) {
            Helper.showAlert("Cannot find or load the SQL driver");
            return null;
        }

        //Iterate over each connection string, get the result, and save it
        ArrayList<String> results = new ArrayList<>();

        for (String databaseName : itemSQL.getDatabaseNames()) {
            try {
                connect = DriverManager.getConnection(buildConnectionString(databaseName));
                statement = connect.createStatement();

                resultSet = statement.executeQuery(buildStatementString(databaseName));

                results.add(getResult(resultSet));
            } catch (SQLException sqlException) {
                close();
                return new Pair(ReturnType.ERROR, sqlException.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        //Compare each result and return null if they do not match, or the definition if they do
        String definitionToCompare = null;

        for (String definition : results) {
            //Remove all spaces, tabs, and new lines
            String trimmedDefinition = definition.replace("\n", "").replace("\t", "").replace(" ", "");

            //If this is the first iteration, simply set the definition to compare against
            if (definitionToCompare == null)
                definitionToCompare = trimmedDefinition;
            else {
                //If the previous definition and the current definition do not match, then we already know there is a mismatch
                if (!trimmedDefinition.equalsIgnoreCase(definitionToCompare))
                    return new Pair(ReturnType.VALUE, null);
            }
        }
        return new Pair(ReturnType.VALUE, definitionToCompare);
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

    protected boolean isValidConnectionString() {
        if (itemSQL.getHost() == null || itemSQL.getHost().isEmpty()) {
            Helper.showAlert("The value given for 'host' is empty or cannot be found");
            return false;
        } else if (!itemSQL.usesIntegratedSecurity()) {
            if (itemSQL.getUsername() == null || itemSQL.getUsername().isEmpty()) {
                Helper.showAlert("The value given for 'username' is empty or cannot be found");
                return false;
            } else if (itemSQL.getPassword() == null || itemSQL.getPassword().isEmpty()) {
                Helper.showAlert("The value given for 'password' is empty or cannot be found");
                return false;
            }
        }
        return true;
    }

}
