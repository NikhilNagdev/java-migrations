package database.migrations;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import constants.Files;
import files.FileOperation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationCreator {

    public MigrationCreator(){
        this.fileOperation = new FileOperation();
    }

    /**
     * This method is used to create a migration file in database/migration folder
     * @param name the table name for the migration file
     */
    public void createMigration(String name){
        //checking if migration file was already existing or not
        if(!this.checkIfMigrationFileExists(name)){
            String migrationFileName = this.getMigrationFileName(name);
            //creating migration with a default template
            if(this.isMigrationTypeCreate(name))
                this.fileOperation.createFileWithContent("database\\migrations", migrationFileName, constants.Files.CREATE_TABLE_MIGRATION_STRUCTURE);
            else
                this.fileOperation.createFileWithContent("database\\migrations", migrationFileName, Files.ALTER_TABLE_MIGRATION_STRUCTURE);
            System.out.println("Migration file created " + "\"" + migrationFileName + "\"");
        }else{
            System.out.println("Migration file already exists");
        }
    }

    /**
     * This method is used to check if the migration file is existing
     * in the database/migration folder
     * @param name the table name for the migration file
     * @return true if file exists otherwise false
     */
    public boolean checkIfMigrationFileExists(String name){
        //Getting all the files from migration folder
        List<String> fileNames = fileOperation.getFileNamesFromFolder("database\\migrations");
        //Creating a pattern
        Pattern pattern = Pattern.compile(name + ".json");
        Matcher matcher;
        for(String fileName : fileNames){
            matcher = pattern.matcher(fileName);
            if (matcher.find()){//checking if pattern is matching with the files in the folder or not
//                System.out.println("TRUE");
                return true;
            }
        }
        return false;
    }


    /**
     * This method generates a migration file name current timestamp
     * @param name the table name for the migration file
     * @return migration file name
     */
    public String getMigrationFileName(String name){
        return this.getCurrentTimestamp() + "_" + name + ".json";
    }

    /**
     * @return the current timestamp
     */
    private String getCurrentTimestamp(){
        return new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
    }

    private boolean isMigrationTypeCreate(String name){
        return Pattern.compile("[0-9]{4}_([0-9]{2}_){2}[0-9]{6}_create_.*.json").matcher(name).find();
    }

    private FileOperation fileOperation;

}
