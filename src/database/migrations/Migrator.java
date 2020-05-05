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

public class Migrator {

    public Migrator(){
        this.parser = new Parser("database");
        this.crud = new CRUD(parser.getDatabase());
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
        List<String> allMigrations = this.fileOperation.getFileNamesFromFolder("database\\migrations");
        List<String> ranMigrations = this.migration.getRanMigrations();

        //getting the paths of all migration files
        List<String> paths = this.fileOperation.getAllPathsMigration( "database\\migrations");
        int i=0;
        //Running a single single migration
        for (String migrationName : allMigrations) {
            //the migration should not be already ran
            if(!ranMigrations.contains(migrationName)){
                flag = runMigration(migrationName, flag, parser.getTable(migrationName, paths.get(i++)));
            }
        }
        System.out.println((flag ? "Nothing to migrate..." : "Migrated Successfully"));
    }


    /**
     * This method is used to run the given migration
     * @param migrationName migration file to run
     * @param flag
     * @param table Table object generated from migration file
     * @return false if migration ran
     */
    private boolean runMigration(String migrationName, boolean flag, Table table){
        if(Helper.getFileType(migrationName).equals(Files.CREATE)){
            return runCreateMigration(table, migrationName);
        }else if(Helper.getFileType(migrationName).equals(Files.ALTER_ADD)){
            return runAlterMigration(table, migrationName, Files.ALTER_ADD);
        }else if(Helper.getFileType(migrationName).equals(Files.ALTER_CHANGE)){
            return runAlterMigration(table, migrationName, Files.ALTER_CHANGE);
        }else if(Helper.getFileType(migrationName).equals(Files.ALTER_DROP)){
            return runAlterMigration(table, migrationName, Files.ALTER_DROP);
        }
       return flag;
    }

    /**
     * This is method is used to run a create migration.
     * @param table Table object generated from migration file.
     * @param migrationName name of the migration file
     * @return false if migration ran
     */
    private boolean runCreateMigration(Table table, String migrationName){
        if(crud.runCreate(this.schemaBuilder.generateTableQuery(table))){
            migration.addMigrationEntry(migrationName);//logging the ran migration
            return false;//false indicates migrations were pending to run
        }
        return true;
    }

    /**
     * This is method is used to run a alter migration.
     * @param table Table object generated from migration file.
     * @param migrationName name of the migration file
     * @param alterType indicates which type of alter is to be ran
     * @return false if migration ran
     */
    private boolean runAlterMigration(Table table, String migrationName, String alterType){
        if(alterType.equals(Files.ALTER_ADD)){
            if(crud.runAlterQueries(this.schemaBuilder.generateAlterTableQuery(table, Files.ALTER_ADD))){
                migration.addMigrationEntry(migrationName);//logging the ran migration
                table.getColumns().addAll(table.getAlterColumns());
                table.getAlterColumns().clear();//clearing all the alter columns as alter is successfully done
                return false;//false indicates migrations are pending to run
            }
        }else if(alterType.equals(Files.ALTER_CHANGE)){
            if(crud.runAlterQueries(this.schemaBuilder.generateAlterTableQuery(table, Files.ALTER_CHANGE))){
                migration.addMigrationEntry(migrationName);//logging the ran migration
                table.addChangedColumnsToTableObj();
                table.getAlterColumns().clear();//clearing all the alter columns as alter is successfully done
                return false;//false indicates migrations are pending to run
            }
        }else if(alterType.equals(Files.ALTER_DROP)){
            if(crud.runAlterQueries(this.schemaBuilder.generateAlterTableQuery(table, Files.ALTER_DROP))){
                migration.addMigrationEntry(migrationName);//logging the ran migration
                table.removeColumnsFromTableAfterDrop();
                table.getAlterColumns().clear();//clearing all the alter columns as alter is successfully done
                return false;//false indicates migrations are pending to run
            }
        }
        return true;
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
        }/*else{
            System.out.println("Migration table is already existing");
        }*/

    }

    //Variable Declaration
    private Parser parser = null;
    private CRUD crud = null;
    private SchemaBuilder schemaBuilder = null;
    private Migration migration = null;
    private FileOperation fileOperation = null;
}
