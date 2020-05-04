package database.migrations;

import database.CRUD;
import database.querybuilder.QueryBuilder;
import files.FileOperation;
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

    /**
     * This methods returns all names of files that are in migration folder
     * @return List of names of migration file
     */
    public List<String> getAllMigrationNames(){
        return this.fileOperation.getFileNamesFromFolder("database\\migrations");
    }

    /**
     * Adding an entry in migration table to indicate that migration was ran
     * @param name
     */
    public void addMigrationEntry(String name){
        List<LinkedHashMap<String, Object>> values = new ArrayList<>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        values.add(map);
        System.out.println(this.queryBuilder.insert(values));
    }


    public String getMigrationName(String tableName){
        List<String> fileNames = fileOperation.getFileNamesFromFolder("database\\migrations");
        Pattern pattern = Pattern.compile("create_" + tableName + "_table.json");
        Matcher matcher = null;
        for(String name : fileNames){
            matcher = pattern.matcher(name);
            if (matcher.find()){
                return name;
            }
        }
        return null;
    }


    /**
     * Checking if the migration table was already in database or not
     * @return true if table exists otherwise false
     */
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


    /**
     * Getting the migrations that were ran
     * @return List of ran migrations
     */
    public List<String> getRanMigrations(){

        List<String> finalResults = new ArrayList<String>();
        if(doMigrationTableExists()){
            List<SortedMap<String, Object>> results
                    = this.queryBuilder
                    .select("*")
                    .get();
            for(SortedMap<String, Object> result : results){
                finalResults.add((String)result.get("name"));
            }
            return finalResults;
        }else{
            return finalResults;
        }

    }


    private FileOperation fileOperation = null;
    private QueryBuilder queryBuilder = null;
    private QueryBuilder tablesInfo = null;
    private CRUD crud = null;
}
