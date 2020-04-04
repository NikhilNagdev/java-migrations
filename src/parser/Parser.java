
package parser;
import javax.json.*;
import java.io.*;


public class Parser {
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

        String name = jsonObject.getString("table_name");

        JsonArray columns = jsonObject.getJsonArray("columns");

        System.out.println(columns);


        for (JsonObject result : columns.getValuesAs(JsonObject.class)) {
//            System.out.print(result.getJsonObject("from").getString("name"));
            for (String key: result.keySet()) {
                System.out.println(key);
            }
        }
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
        System.out.println(name);
    }
}
