package database.migrations;

import files.FileOperation;

import java.util.List;

public class Migration {

    Migration(){
        this.fileOperation = new FileOperation();
    }

    public List<String> getAllMigrationNames(){
        return this.fileOperation.getFileNamesFromFolder("database\\migrations");
    }

    private FileOperation fileOperation = null;
}
