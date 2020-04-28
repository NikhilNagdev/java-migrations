package database.migrations;

import database.CRUD;
import database.querybuilder.QueryBuilder;
import files.FileOperation;
import parser.Parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Migration {

    Migration(CRUD crud){
        this.fileOperation = new FileOperation();
        this.queryBuilder = new QueryBuilder("migrations", crud);
        this.tablesInfo = new QueryBuilder("INFORMATION_SCHEMA.TABLES", crud);
        this.crud = crud;
    }

    public List<String> getAllMigrationNames(){
        return this.fileOperation.getFileNamesFromFolder("database\\migrations");
    }

    public void addMigrationEntry(String tableName){
        List<LinkedHashMap<String, Object>> values = new ArrayList<>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("name", this.getMigrationName(tableName));
        values.add(map);
        System.out.println(this.queryBuilder.insert(values));
    }

    public String getMigrationName(String tableName){
        List<String> fileNames = fileOperation.getFileNamesFromFolder("database\\migrations");
        Pattern pattern = Pattern.compile("create_" + tableName + ".json");
        Matcher matcher = null;
        for(String name : fileNames){
            matcher = pattern.matcher(name);
            if (matcher.find()){
                return name;
            }
        }
        return null;
    }

    public boolean doMigrationTableExists(){

//        System.out.println(this.tablesInfo
//                .select("table_name")
//                .where("table_schema", this.crud.getDatabase().getDbName())
//                .andWhere("table_name", "migrations")
//                .get());
        return !(
                this.tablesInfo
                .select("table_name")
                .where("table_schema", this.crud.getDatabase().getDbName())
                .andWhere("table_name", "migrations")
                .get().isEmpty()
        );

    }

    public List<String> getRanMigrations(){
        List<String> finalResults = new ArrayList<String>();
        List<SortedMap<String, Object>> results = this.queryBuilder
                                                .select("*")
                                                .get();
        for(SortedMap<String, Object> result : results){
            finalResults.add((String)result.get("name"));
        }
        return finalResults;
    }


    private FileOperation fileOperation = null;
    private QueryBuilder queryBuilder = null;
    private QueryBuilder tablesInfo = null;
    private CRUD crud = null;
}
