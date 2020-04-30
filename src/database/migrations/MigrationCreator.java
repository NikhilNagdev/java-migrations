package database.migrations;

import files.FileOperation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationCreator {

    MigrationCreator(){
        this.fileOperation = new FileOperation();
    }

    /**
     * This method is used to create a migration file in database/migration folder
     * @param type type of the migration file
     * @param tableName the table name for the migration file
     */
    public void createMigration(String type, String tableName){
        //checking if migration file was already existing or not
        if(!this.checkIfMigrationFileExists(type, tableName)){
            String migrationFileName = this.getMigrationFileName(type, tableName);
            //creating migration with a default template
            this.fileOperation.createFileWithContent("database\\migrations", migrationFileName, constants.Files.CREATE_TABLE_MIGRATION_STRUCTURE);
            System.out.println("Migration file created " + "\"" + migrationFileName + "\"");
        }else{
            System.out.println("Migration file already exists");
        }
    }

    /**
     * This method is used to check if the migration file is existing
     * in the database/migration folder
     * @param type type of migration file
     * @param tableName the table name for the migration file
     * @return true if file exists otherwise false
     */
    public boolean checkIfMigrationFileExists(String type, String tableName){
        //Getting all the files from migration folder
        List<String> fileNames = fileOperation.getFileNamesFromFolder("database\\migrations");
        //Creating a pattern
        Pattern pattern = Pattern.compile(type + "_" + tableName + ".json");
        Matcher matcher;
        for(String name : fileNames){
            matcher = pattern.matcher(name);
            if (matcher.find()){//checking if pattern is matching with the files in the folder or not
//                System.out.println("TRUE");
                return true;
            }
        }
        return false;
    }


    /**
     * This method generates a migration file name current timestamp
     * @param type type of migration file
     * @param tableName the table name for the migration file
     * @return migration file name
     */
    public String getMigrationFileName(String type, String tableName){
        return this.getCurrentTimestamp() + "_" + type + "_" + tableName + ".json";
    }

    /**
     * @return the current timestamp
     */
    private String getCurrentTimestamp(){
        return new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
    }

    private FileOperation fileOperation;

}
