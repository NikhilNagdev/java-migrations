package database;

import parser.Parser;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.*;

public class Database {

    private String dbUsername = "";

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Database getAllAttributes(){
        Parser p = new Parser("");
        return new Parser("").getDatabase();
    }

    /**
     * This method is used get the connection string that has to be used while generating Connection object.
     * @return connection string
     */
    public String getConnectionString(){
        return this.getDbUrl() + this.getDbHost() + ":" + this.getDbPort() + "/" + this.getDbName();
    }

    /**
     * This method is used to get the Connection object.
     * @return Connection object
     */
    public Connection getConnection(){
        try {
            this.setConnection(DriverManager.getConnection(this.getConnectionString(), this.getDbUsername(), this.getDbPassword()));
            return this.connection;
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to database " + e);
        }
        return null;
    }

    private String dbPassword = "";
    private String dbName = "";
    private String dbPort = "";
    private String dbHost = "";
    private String dbUrl = "";
    private Connection connection = null;

}