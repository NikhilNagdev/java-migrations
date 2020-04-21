package database.migrations;

import database.CRUD;
import database.Table;
import database.querybuilder.QueryBuilder;
import parser.Parser;

import java.util.List;

public class Migrator {

    public Migrator(){
        this.parser = new Parser("database");
        this.crud = new CRUD(parser.getDatabase());
        this.queryBuilder = new QueryBuilder("", null);
    }

    public void runMigrations(){
        List<Table> tables = parser.getTables();
        for(Table table : tables){
//            System.out.println(queryBuilder.generateTableQuery(table));
            if(crud.runCreate(queryBuilder.generateTableQuery(table)))
                System.out.println("Table created");
            else
                System.out.println("There was some problem while creating table");
            System.out.println();
        }
    }

    Parser parser = null;
    CRUD crud = null;
    QueryBuilder queryBuilder = null;
}
