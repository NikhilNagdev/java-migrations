package database;

import database.querybuilder.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedMap;

public class CRUD {


//    public CRUD(){
//        this.connection =
//    }
    public static QueryBuilder table(String tableName) {

        return new QueryBuilder(tableName, new CRUD());

    }



    public List<SortedMap<String, Object>> runSelect(String query) {

        try{
            PreparedStatement stmt = this.connection.prepareStatement("select * from emp");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }
        }catch (SQLException se) {

        }
        return null;

    }

    private Connection connection = null;
}


