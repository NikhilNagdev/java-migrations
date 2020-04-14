package database;

import database.querybuilder.QueryBuilder;

public class CRUD {

    public static QueryBuilder table(String tableName){

        return new QueryBuilder(tableName, new CRUD());

    }



}
