
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
//        mainReaderObject =  Json.createReader(getFileInputStream(pathToFile));
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

        //we can close IO resource and JsonReader now
        jsonReader.close();
        fis.close();
//        String name = jsonObject.getString("table_names");

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

    public Table getTable(){
        return null;
    }

    public Column[] getColumns(){
        JsonObject table = mainReaderObject.readObject();
        JsonArray columns = table.getJsonArray("columns");
        List<Column> returnColumns = new ArrayList<Column>();
        JsonObject columnAttributes = null;

        for (JsonObject column : columns.getValuesAs(JsonObject.class)){
            Column columnObj = new Column();
            for(String name : column.keySet()){
                columnObj.setColumnName(name);
                columnAttributes = column.getJsonObject(name);
            }
            if(!(columnAttributes.getString("datatype") == null)){
                columnObj.setDatatype(columnAttributes.getString("datatype"));
            }else{
                System.out.println("Datatype attribute is missing for column name " + columnObj.getColumn_name());
            }


        }

        return null;
    }

    private JsonReader mainReaderObject = null;
}
