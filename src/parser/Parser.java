
package parser;
import database.Column;
import database.Table;
import files.Files;

import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Parser {

    public Parser(String pathToFile){
        mainReaderObject =  Json.createReader(getFileInputStream("F:\\Programming\\Java\\Projects\\java-migrations\\db.json"));
        configReaderObject = Json.createReader(getFileInputStream("F:\\Programming\\Java\\Projects\\java-migrations\\dbconfig.json"));
        jsonTableObject = mainReaderObject.readObject();
        configJsonObject = configReaderObject.readObject();
    }

    private FileInputStream getFileInputStream(String path){
        return Files.getFileInputStream(path);
    }

    public void meth() throws IOException {
        InputStream fis = new FileInputStream("F:\\Programming\\Java\\Projects\\java-migrations\\db.json");

        //create JsonReader object
        JsonReader jsonReader = Json.createReader(fis);

        /**
         * We can create JsonReader from Factory also
         JsonReaderFactory factory = Json.createReaderFactory(null);
         jsonReader = factory.createReader(fis);
         */

        //get JsonObject from JsonReader
        JsonObject jsonObject = jsonReader.readObject();
        getTable();
        //we can close IO resource and JsonReader now
        jsonReader.close();
        fis.close();
//        String name = jsonObject.getString("table_names");
//        System.out.println(jsonObject.getInt("length"));
        JsonArray columns = jsonObject.getJsonArray("columnss");
//        System.out.println(name);


//        for (JsonObject result : columns.getValuesAs(JsonObject.class)) {
////            System.out.print(result.getJsonObject("from").getString("name"));
//            for (String key: result.keySet()) {
//                System.out.println(key);
//            }
//        }
//
//        //reading arrays from json
//        JsonArray jsonArray = jsonObject.getJsonArray("phoneNumbers");
//        long[] numbers = new long[jsonArray.size()];
//        int index = 0;
//        for(JsonValue value : jsonArray){
//            numbers[index++] = Long.parseLong(value.toString());
//        }
//        emp.setPhoneNumbers(numbers);
//
//        //reading inner object from json object
//        JsonObject innerJsonObject = jsonObject.getJsonObject("address");
//        Address address = new Address();
//        address.setStreet(innerJsonObject.getString("street"));
//        address.setCity(innerJsonObject.getString("city"));
//        address.setZipcode(innerJsonObject.getInt("zipcode"));
//        emp.setAddress(address);
//
//        //print employee bean information
//        System.out.println(name);
    }


    /**
     * This method is used to return the table object that is created as per JSON file
     * @return table object
     */
    public Table getTable(){
        Table table = new Table();
        table.setTableName(jsonTableObject.getString("table_name"));
        table.setColumns(getColumns());
        System.out.println(table);
        return table;
    }


    /**
     * This method is used to return the list of columns that are specified JSON file
     * @return List of column objects
     */
    public List<Column> getColumns(){
        JsonArray columns = jsonTableObject.getJsonArray("columns");
        List<Column> columnList = new ArrayList<Column>();
        for (JsonObject column : columns.getValuesAs(JsonObject.class)){
            columnList.add(getColumn(column));
        }
        System.out.println(columnList.size());
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

        if(!(columnAttributes.getString("datatype") == null)){
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
            if(columnObj.getDatatype().equalsIgnoreCase("timestamp")){
                columnObj.setDefault_value(columnAttributes.getString("default_value"));
            }else if(columnObj.getDatatype().equalsIgnoreCase("string")){
                columnObj.setDefault_value(columnAttributes.get("default_value"));
            }else{
                columnObj.setDefault_value(columnAttributes.getInt("default_value"));
            }

        }
        if((columnAttributes.containsKey("unsigned"))){
            columnObj.setUnsigned(columnAttributes.getBoolean("unsigned"));
        }

    }

    public void getConfigAttributes(JsonObject configJsonObject){

    }

    private JsonReader mainReaderObject = null;
    private JsonReader configReaderObject = null;
    private JsonObject jsonTableObject = null;
    private JsonObject configJsonObject = null;
}
