
package parser;
import database.Column;
import database.Database;
import database.Table;
import files.FileOperation;
import helper.Helper;

import javax.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Parser {

    public Parser(String rootFolderPath){
        root = rootFolderPath;
        files = new FileOperation();
        setJsonConfigObjects(rootFolderPath);
        this.tableObjectsMap = new HashMap<String, Table>();
    }


    /**
     * This method sets the readerObj with the specified path and then jsonTableObject is set by readerObj
     * @param path path of the migration file
     */
    public void setJsonTableObject(String path){
        mainReaderObject =  Json.createReader(files.getFileInputStream(path));
        jsonTableObject = mainReaderObject.readObject();
    }


    /**
     * This method sets the readerObj with the config file path and then jsonTableObject is set by readerObj
     * @param path path of the migration file
     */
    public void setJsonConfigObjects(String path){
        configReaderObject = Json.createReader(files.getFileInputStream(path + "\\dbconfig.json"));
        configJsonObject = configReaderObject.readObject();
    }

    /**
     * This method returns the list of table objects generated as per migrations files
     * @return list of table objects
     */
    public Map<String, Table> getTables(){
        Map<String, Table> tableMap = new HashMap<String, Table>();
        List<Table> tables = new ArrayList<Table>();
        //getting path of all files that are in migration folder
        List<String> paths = files.getAllPathsMigration(this.root + "\\migrations");
        for(String path : paths){
            setJsonTableObject(path);//setting jsonreader object and jsontable object as per one file
//            tables.add(getTable());
//            Table table = getTable(path);
//            tableMap.put(Helper.getFileType(path) + "_" + table.getTableName(), table);
//            this.tableObjectsMap.put(table.getTableName(), table);
        }
        return tableMap;
    }

    /**
     * This method is used to return the table object that is created as per JSON file
     * @return table object
     */
    public Table getTable(String path, String p){
        boolean flag = false;
        Table table = new Table();
        setJsonTableObject(p);
        String tableName = jsonTableObject.getString("table_name");
        table.setTableName(tableName);
        if(jsonTableObject.containsKey("alter_columns")){
            if(this.tableObjectsMap.containsKey(tableName)){
                if(this.tableObjectsMap.get(tableName).getAlterColumns() == null){
                    this.tableObjectsMap.get(tableName).setAlterColumns(getColumns());
                }
                else{
//                    this.tableObjectsMap.get(tableName).getAlterColumns().addAll(getColumns());
//                    this.tableObjectsMap.get(tableName).setAlterColumns(
//                            this.tableObjectsMap.get(tableName).getAlterColumns()
//                    );

                    for(Column column : getColumns()){
                        if(this.tableObjectsMap.get(tableName).getAlterColumns().contains(column)){
                            this.tableObjectsMap.get(tableName)
                                    .getAlterColumns()
                                    .set(this.tableObjectsMap.get(tableName).getAlterColumns().indexOf(column), column);
                            flag = true;
                        }else{
                            this.tableObjectsMap.get(tableName)
                                    .getAlterColumns()
                                    .add(column);
                        }
                    }
//                    if(!flag)
//                        this.tableObjectsMap.get(tableName).setAlterColumns(
//                                this.tableObjectsMap.get(tableName).getAlterColumns()
//                        );
                }
                //this is done because we have to setAlterColumns of the table that is existing
                return this.tableObjectsMap.get(tableName);
            }else{
                System.out.println("Table " + tableName + " doesn't exists");
            }
        }else if(jsonTableObject.containsKey("drop_columns")){
            table.setAlterColumns(getColumns());
        }else if(jsonTableObject.containsKey("columns")){
            table.setColumns(getColumns());
        }
        this.tableObjectsMap.put(table.getTableName(), table);
        return table;
    }

    /**
     * This method is used to return the list of columns that are specified JSON file
     * @return List of column objects
     */
    public List<Column> getColumns(){
        JsonArray columns = null;
        if(jsonTableObject.containsKey("alter_columns")){
            columns = jsonTableObject.getJsonArray("alter_columns");
        }else if(jsonTableObject.containsKey("drop_columns")){
            columns = jsonTableObject.getJsonArray("drop_columns");
            return getAlterDropColumn(columns);
        }else if(jsonTableObject.containsKey("columns")){
            columns = jsonTableObject.getJsonArray("columns");
        }
        List<Column> columnList = new ArrayList<Column>();
        for (JsonObject column : columns.getValuesAs(JsonObject.class)){
            columnList.add(getColumn(column));
        }
//        System.out.println(columnList);
        return columnList;
    }

    public List<Column> getAlterDropColumn(JsonArray columns){
        List<Column> finalList = new ArrayList<Column>();
        for(JsonValue q : columns){
            Column column = new Column();
            column.setColumn_name(q.toString().substring(1, q.toString().length()-1));
            finalList.add(column);
        }
        return finalList;
    }

    /**
     * This method is used to return a single columnObj which is generated as per JSON file
     * @param column is JsonObject by which Column class object has to be created
     * @return Column class object
     */
    public Column getColumn(JsonObject column){
        Column columnObj = new Column();
        JsonObject columnAttributes = null;
        for(String name : column.keySet()){
            columnObj.setColumnName(name);
            columnAttributes = column.getJsonObject(name);
        }
        setColumnAttributes(columnObj, columnAttributes);
        return columnObj;
    }

    /**
     * This method is used to set attributes to the Column class obj as per the attributes defined in the JSON file
     * @param columnObj the Column class object in which the attributes has to be set
     * @param columnAttributes JsonObject from which the attributes has to be fetched to set it in the Column class object
     */
    public void setColumnAttributes(Column columnObj, JsonObject columnAttributes){

        if(columnAttributes.getString("datatype") != null){
            columnObj.setDatatype(columnAttributes.getString("datatype"));
        }else{
            System.out.println("Datatype attribute is missing for column name " + columnObj.getColumn_name());
        }

        if((columnAttributes.containsKey("primary_key"))){
            columnObj.setIs_primary_key(columnAttributes.getBoolean("primary_key"));
        }
        if((columnAttributes.containsKey("length"))){
            columnObj.setLength(columnAttributes.getInt("length"));
        }
        if((columnAttributes.containsKey("default_value"))){
            //This if help checks the datatype of column so as the default value can be set as string, int etc
            if(columnObj.getDatatype().equalsIgnoreCase("timestamp")){
                columnObj.setDefault_value(columnAttributes.getString("default_value"));//default value should be string
            }else if(columnObj.getDatatype().equalsIgnoreCase("string")){
                columnObj.setDefault_value(columnAttributes.get("default_value"));//default value should be enclosed in "" codes
            }else {
                columnObj.setDefault_value(columnAttributes.getInt("default_value"));//if default value is other than string it should be int
            }

        }

        if((columnAttributes.containsKey("unsigned"))){
            columnObj.setUnsigned(columnAttributes.getBoolean("unsigned"));
        }

        if(columnAttributes.containsKey("foreign_key")){
            JsonObject foreignKeyAttributes = columnAttributes.getJsonObject("foreign_key");
            Map<String, String> foreignKeyAttributesMap = new HashMap<String, String>();
            for(String key : foreignKeyAttributes.keySet()){
                //setting attributes of foreign key in a Map
                foreignKeyAttributesMap.put(key, foreignKeyAttributes.getString(key));
            }
            columnObj.setIsForeignKey(true);
            columnObj.setForeignKeyAttributes(foreignKeyAttributesMap);
        }

    }

    /**
    * This method sets the Database objects with attributes as per config file
     * @return  the datatabse object
    */
    public Database setConfigAttributes(){

        Database db = new Database();
        db.setDbUrl(configJsonObject.getString("URL"));
        db.setDbHost(configJsonObject.getString("DB_HOST"));
        db.setDbPort(configJsonObject.getString("DB_PORT"));
        db.setDbName(configJsonObject.getString("DB_NAME"));
        db.setDbUsername(configJsonObject.getString("DB_USERNAME"));
        db.setDbPassword(configJsonObject.getString("DB_PASSWORD"));
        return db;

    }

    /**
     * This method is sed to get the database object that is set as per config file
     * @return database object
     * */
    public Database getDatabase(){
        return setConfigAttributes();
    }

    //Variable Declarations
    private JsonReader mainReaderObject = null;
    private JsonReader configReaderObject = null;
    private JsonObject jsonTableObject = null;
    private JsonObject configJsonObject = null;
    private int noOfTables = 0;
    private FileOperation files = null;
    private String root = "";
    private Map<String, Table> tableObjectsMap = null;
}
