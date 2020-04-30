
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
     * This method is used to return the table object that is created as per JSON file
     * @return table object
     */
    public Table getTable(){
        Table table = new Table();
        table.setTableName(jsonTableObject.getString("table_name"));
        table.setColumns(getColumns());
        return table;
    }


    /**
     * This method returns the list of table objects generated as per migrations files
     * @return list of table objects
     */
    public Map<String, Table> getTables(){
        Map<String, Table> tableMaps = new HashMap<String, Table>();
        List<Table> tables = new ArrayList<Table>();
        //getting path of all files that are in migration folder
        List<String> paths = files.getAllPathsMigration(this.root + "\\migrations");
        for(String path : paths){
            setJsonTableObject(path);//setting jsonreader object and jsontable object as per one file
//            tables.add(getTable());
            Table table = getTable();
            tableMaps.put(Helper.getFileType(path) + "_" + table.getTableName(), table);
            System.out.println(Helper.getFileType(path));
        }
        return tableMaps;
    }


    /**
     * This method is used to return the list of columns that are specified JSON file
     * @return List of column objects
     */
    public List<Column> getColumns(){
        JsonArray columns = jsonTableObject.containsKey("alter_columns") ? jsonTableObject.getJsonArray("alter_columns") : jsonTableObject.getJsonArray("columns");
        List<Column> columnList = new ArrayList<Column>();
        for (JsonObject column : columns.getValuesAs(JsonObject.class)){
            columnList.add(getColumn(column));
        }
        return columnList;
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
}
