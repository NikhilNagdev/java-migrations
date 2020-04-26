package database.migrations;

import database.CRUD;
import database.querybuilder.QueryBuilder;
import files.FileOperation;
import parser.Parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Migration {

    Migration(CRUD crud){
        this.fileOperation = new FileOperation();
        this.queryBuilder = new QueryBuilder("migrations", crud);
    }

    public List<String> getAllMigrationNames(){
        return this.fileOperation.getFileNamesFromFolder("database\\migrations");
    }

    public void addMigrationEntry(String migrationName){
        List<LinkedHashMap<String, Object>> values = new ArrayList<>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("name", migrationName);
        values.add(map);
        System.out.println(this.queryBuilder.insert(values));
    }

    private FileOperation fileOperation = null;
    private QueryBuilder queryBuilder = null;
}
