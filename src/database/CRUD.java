package database;

import database.querybuilder.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
            return resultSetToCollection(this.preparedStatement.executeQuery());
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

    private List<SortedMap<String, Object>> resultSetToCollection(ResultSet resultSet){
        List<SortedMap<String, Object>> result = new ArrayList<SortedMap<String, Object>>();
        try{
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while(resultSet.next()){
                SortedMap<String, Object> map = new TreeMap<String, Object>();
                for(int i=1; i<=columnCount; i++){
                    map.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
                result.add(map);
            }
            return result;
        }catch(SQLException e){

        }
        return null;

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


