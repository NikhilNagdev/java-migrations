package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnect {

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/restodb", "root", "");
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to database " + e);
        }
        return null;
    }
}