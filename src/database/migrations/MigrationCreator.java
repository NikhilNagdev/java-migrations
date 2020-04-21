package database.migrations;

import files.FileOperation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationCreator {


    public void createMigration(String type, String tableName){
        if(!this.checkIfMigrationFileExists(type, tableName)){
            this.f.createFileWithContent("database\\migrations", this.getMigrationFileName(type, tableName), constants.Files.CREATE_TABLE_MIGRATION_STRUCTURE);
            System.out.println("Migration file created");
        }else{
            System.out.println("Migration file already exists");
        }
    }

    public boolean checkIfMigrationFileExists(String type, String tableName){
        List<String> fileNames = f.getFileNamesFromFolder("database\\migrations");
        Pattern pattern = Pattern.compile(type + "_" + tableName + ".json");
        Matcher matcher = null;
        for(String name : fileNames){
            matcher = pattern.matcher(name);
            if (matcher.find()){
//                System.out.println("TRUE");
                return true;
            }
        }
        return false;
    }

    public String getMigrationFileName(String type, String tableName){
        return this.getCurrentTimestamp() + "_" + type + "_" + tableName + ".json";
    }

    private String getCurrentTimestamp(){
        return new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
    }

    FileOperation f = new FileOperation();

}
