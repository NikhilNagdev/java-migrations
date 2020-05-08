package database;

import database.querybuilder.QueryBuilder;

import java.sql.*;
import java.util.*;

public class CRUD {


    public CRUD(Database database){
        initVariables(database);
    }

    /**
     * This method is used to initialize al the variables of this class.
     * @param database This is Database object from which Connection obj has to be taken to run queries
     */
    public void initVariables(Database database){
        this.database = database;
        this.connection = database.getConnection();
        try{
            this.statement = connection.createStatement();
        }catch(SQLException se){
            System.out.println("Exception while initializing statement: " + se);
        }
    }

    /**
     * This method is used to run the select query.
     * It first binds all the values to the select query string and then executes the select query.
     * @param query The select query
     * @param bindings The bindings to be bind
     * @return Result of the select query in a Collection
     */
    public List<LinkedHashMap<String, Object>> runSelect(String query, List<String> bindings) {
        try{
            this.preparedStatement = this.connection.prepareStatement(query);
            for(int i=0; i<bindings.size(); i++){
                this.preparedStatement.setObject(i+1, (bindings.get(i)));
            }
//            System.out.println(preparedStatement);
            return resultSetToCollection(this.preparedStatement.executeQuery());
        }catch (SQLException se) {
            System.out.println("Exception while running select " + se);
        }
        return null;
    }

    /**
     * This method is used to run the create query.
     * @param query The create query
     * @return true if create query is executed otherwise false
     */
    public boolean runCreate(String query){
        System.out.println(query);
        try {
            this.statement.execute(query);
            return true;
        }catch(SQLException se){
            System.out.println("Exception while creating tables: " + se);
        }
        return true;
    }

    /**
     * This method is used to run the insert query.
     * It first binds all the values to the insert query string and then executes the insert query.
     * @param query The insert query
     * @param bindings The bindings to be bind
     * @return true if insert query was successfully ran otherwise false
     */
    public boolean runInsert(String query, List<List<Object>> bindings){
        int i = 1;
        try{
            this.preparedStatement = this.connection.prepareStatement(query);
            for(List<Object> binding : bindings){
                for(Object value : binding){
                    this.preparedStatement.setObject(i, value);
                    i++;
                }
            }
            return this.preparedStatement.executeUpdate() > 0;
        }catch (SQLException se) {
            System.out.println("Exception while inserting " + se);
        }
        return false;
    }

    /**
     * This method takes the alter queries and runs it.
     * @param queries the alter queries to be ran
     * @return
     */
    public boolean runAlterQueries(List<String> queries) {
        boolean flag = true;
        for(String query : queries){
            if(flag)//flag is used to keep track if the query was ran
                flag = this.runAlter(query);
            else{
                System.out.println("There was some problem altering table ");
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to run the alter query
     * @param query the query to be ran
     * @return true if query was successfully ran
     */
    public boolean runAlter(String query) {
        System.out.println(query);
        try {
            this.statement.execute(query);
            return true;
        }catch(SQLException se){
            System.out.println("Exception while altering table: " + se);
        }
        return false;
    }

    /**
     * This method is used to convert the ResultSet results to a Collection to easily access the result
     * @param resultSet the result set to be converted
     * @return Collection of ResultSet results
     */
    private List<LinkedHashMap<String, Object>> resultSetToCollection(ResultSet resultSet){
        List<LinkedHashMap<String, Object>> result = new ArrayList<LinkedHashMap<String, Object>>();
        try{
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();//getting the column names that are in ResultSet
            int columnCount = resultSetMetaData.getColumnCount();
            while(resultSet.next()){
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                for(int i=1; i<=columnCount; i++){
                    //binding the row value and column accordingly
                    map.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
                result.add(map);
            }
            return result;
        }catch(SQLException e){
            System.out.println("Exception while converting result set to a collection " + e);
        }
        return null;
    }

    public Database getDatabase(){
       return this.database;
    }

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private Database database = null;

}


