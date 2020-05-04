package database.migrations;

import constants.Files;
import database.CRUD;
import database.Column;
import database.Table;
import database.querybuilder.QueryBuilder;
import database.querybuilder.SchemaBuilder;
import files.FileOperation;
import helper.Helper;
import parser.Parser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Migrator {

    public Migrator(){
        this.parser = new Parser("database");
        this.crud = new CRUD(parser.getDatabase());
        this.queryBuilder = new QueryBuilder("", null);
        this.schemaBuilder = new SchemaBuilder();
        this.migration = new Migration(crud);
        this.fileOperation = new FileOperation();
    }


    /**
     * This method is used to run the migrations in migrations folder.
     */
    public void runMigrations() {
        boolean flag = true;
        createMigrationTable();//creating a migration table to keep a track of migrations that were already ran
        /*parser.getTables()*/;//getting the Table objects that were created by parser as per the migration files
        List<String> allMigrations = this.fileOperation.getFileNamesFromFolder("database\\migrations");
        List<String> ranMigrations = this.migration.getRanMigrations();

        List<String> paths = this.fileOperation.getAllPathsMigration( "database\\migrations");
        int i=0;
        for (String migrationName : allMigrations) {
            flag = runMigration(migrationName, /*tableMap,*/ ranMigrations, flag, parser.getTable(migrationName, paths.get(i++)));
        }
        System.out.println((flag ? "Nothing to migrate..." : "Migrated Successfully"));
    }

    private boolean runMigration(String migrationName, /*Map<String, Table> tableMap*/ List<String> ranMigrations, boolean flag
    , Table table){
//        Table table = tableMap.get(Helper.getFileType(migrationName) + "_" + Helper.getTableNameFromFileName(migrationName));
//        System.out.println(Helper.getFileType(migrationName) + "_" + Helper.getTableNameFromFileName(migrationName));
        if(!ranMigrations.contains(migrationName)){
            if(this.isMigrationTypeCreate(migrationName)){

                if(crud.runCreate(this.schemaBuilder.generateTableQuery(table))){
                    migration.addMigrationEntry(migrationName);//logging the ran migration
                    if(flag)
                        return false;//false indicates migrations are pending to run
                }
            }else if(Helper.getFileType(migrationName).equals(Files.ALTER_ADD)){
                if(crud.runAlterQueries(this.schemaBuilder.generateAlterTableQuery(table, Files.ALTER_ADD))){
                    migration.addMigrationEntry(migrationName);//logging the ran migration
                    table.getColumns().addAll(table.getAlterColumns());
                    table.getAlterColumns().clear();
                    if(flag)
                        return false;//false indicates migrations are pending to run
                }
            }else if(Helper.getFileType(migrationName).equals(Files.ALTER_CHANGE)){
                if(crud.runAlterQueries(this.schemaBuilder.generateAlterTableQuery(table, Files.ALTER_CHANGE))){
                    migration.addMigrationEntry(migrationName);//logging the ran migration
                    table.getColumns().addAll(table.getAlterColumns());
                    if(flag)
                        return false;//false indicates migrations are pending to run
                }
            }
        }
       return flag;
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
            this.crud.runCreate(this.schemaBuilder.generateTableQuery(table));
            isMigrationTableCreated = true;
        }/*else{
            System.out.println("Migration table is already existing");
        }*/

    }

    private boolean isMigrationTypeCreate(String name){
        return Pattern.compile("[0-9]{4}_([0-9]{2}_){2}[0-9]{6}_create_.*.json").matcher(name).find();
    }

    //Variable Declaration
    private Parser parser = null;
    private CRUD crud = null;
    private QueryBuilder queryBuilder = null;
    private SchemaBuilder schemaBuilder = null;
    private Migration migration = null;
    private static boolean isMigrationTableCreated;
    private FileOperation fileOperation = null;
}
