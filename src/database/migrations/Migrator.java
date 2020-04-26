package database.migrations;

import database.CRUD;
import database.Column;
import database.Table;
import database.querybuilder.QueryBuilder;
import parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class Migrator {

    public Migrator(){
        this.parser = new Parser("database");
        this.crud = new CRUD(parser.getDatabase());
        this.queryBuilder = new QueryBuilder("", null);
        this.migration = new Migration(crud);
    }

    public void runMigrations(){
        createMigrationTable();
        List<Table> tables = parser.getTables();
        for(Table table : tables){
            if(crud.runCreate(queryBuilder.generateTableQuery(table)))
                System.out.println("Table created");
            else
                System.out.println("There was some problem while creating table");
            System.out.println();
        }
    }

    private void createMigrationTable(){
        Table table = new Table();
        table.setTableName("migrations");
        List<Column> columns = new ArrayList<Column>();
        Column column = new Column();

        column.setColumn_name("id");
        column.setDatatype("biginteger");
        column.setUnsigned(true);
        column.setIs_primary_key(true);
        column.setNot_null(true);

        Column migrationName = new Column();

        migrationName.setColumn_name("name");
        migrationName.setDatatype("string");
        migrationName.setNot_null(true);

        Column isMigrationRan = new Column();

        isMigrationRan.setColumn_name("isMigrationRan");
        isMigrationRan.setDatatype("tinyint");
        isMigrationRan.setLength(1);
        isMigrationRan.setNot_null(true);

        columns.add(column);
        columns.add(migrationName);
        columns.add(isMigrationRan);
        table.setColumns(columns);
        System.out.println(table);
        this.crud.runCreate(this.queryBuilder.generateTableQuery(table));
        System.out.println("Help");
    }

    private Parser parser = null;
    private CRUD crud = null;
    private QueryBuilder queryBuilder = null;
    private Migration migration = null;
}
