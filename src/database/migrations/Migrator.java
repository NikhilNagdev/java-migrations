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


    /**
     * This method is used to run the migrations in migrations folder.
     */
    public void runMigrations(){
        boolean flag = true;
        createMigrationTable();//creating a migration table to keep a track of migrations that were already ran
        List<Table> tables = parser.getTables();//getting the Table objects that were created by parser as per the migration files
        List<String> ranMigrations = this.migration.getRanMigrations();
        for(Table table : tables){
            //checking if migration was already ran
            if(!ranMigrations.contains(this.migration.getMigrationName(table.getTableName()))){
                if(crud.runCreate(queryBuilder.generateTableQuery(table))){
                    migration.addMigrationEntry(table.getTableName());//logging the ran migration
                    System.out.println("Table created");
                    if(flag)
                        flag = false;//false indicates migrations are pending to run
                }
                else
                    System.out.println("There was some problem while creating table");
            }else{
                if(!flag)
                    flag = true;
            }
        }
        if(flag){
            System.out.println("All Migrations are Migrated...");
        }else{
            System.out.println("Migrated Successfully");
        }
    }


    /**
     * This method is used to create a migration table in database to keep
     * track of ran migrations.
     */
    private void createMigrationTable(){

        if (!migration.doMigrationTableExists()) {
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
            isMigrationTableCreated = true;
        }/*else{
            System.out.println("Migration table is already existing");
        }*/

    }

    //Variable Declaration
    private Parser parser = null;
    private CRUD crud = null;
    private QueryBuilder queryBuilder = null;
    private Migration migration = null;
    private static boolean isMigrationTableCreated;
}
