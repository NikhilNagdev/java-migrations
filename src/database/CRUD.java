package database;

import database.querybuilder.QueryBuilder;

import java.sql.*;
import java.util.List;
import java.util.SortedMap;

public class CRUD {


    public CRUD(Database database){
        initVariables(database);
    }

    public void initVariables(Database database){
        this.database = database;
        this.connection = database.getConnection();
        try{
            this.statement = connection.createStatement();
        }catch(SQLException se){
            System.out.println("Exception while initializing statement: " + se);
        }
    }

//    public static QueryBuilder table(String tableName) {
//
////        return new QueryBuilder(tableName, new CRUD());
//
//    }



    public List<SortedMap<String, Object>> runSelect(String query, List<String> bindings) {

        try{
            this.preparedStatement = this.connection.prepareStatement(query);
            for(int i=0; i<bindings.size(); i++){
                this.preparedStatement.setObject(i+1, (bindings.get(i)));
            }
            System.out.println(preparedStatement);
            ResultSet rs = this.preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }
        }catch (SQLException se) {

        }
        return null;

    }

    public boolean runCreate(String query){

//        if(this.isConnectionInitialized){
            try {

                System.out.println("boolean : " + this.statement.execute(query));
            }catch(SQLException se){
                System.out.println("Exception while creating tables: " + se);
            }
//        }else{
            this.initConnection();
//        }

        return false;

    }

    public void initConnection(){
        this.connection = this.database.getConnection();
    }

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private Database database = null;
    private boolean isConnectionInitialized;
}


