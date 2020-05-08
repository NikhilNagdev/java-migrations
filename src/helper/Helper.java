package helper;

import constants.Files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    /**
     * This method is used to get the type of the migration file.
     * @param fileName filename from which type has to returned
     * @return type of migration file
     */
    public static String getFileType(String fileName){
        Pattern pattern = Pattern.compile("[0-9]{4}_([0-9]{2}_){2}[0-9]{6}_([a-z]+)_.*.json");
        Matcher matcher = pattern.matcher(fileName);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(2);
        }
        if(result.equals("create")){
            return Files.CREATE;
        }else if(result.equals("add")){
            return Files.ALTER_ADD;
        }else if(result.equals("change") || result.equals("modify")){
            return Files.ALTER_CHANGE;
        }else if(result.equals("alter_drop")){
            return Files.ALTER_DROP;
        }
        return "";
    }

    /**
     * This method is used to get the tablename from the migration filename
     * @param fileName filename
     * @return tablename
     */
    public static String getTableNameFromFileName(String fileName){
        Pattern pattern = null;
        if(
                getFileType(fileName).equals(Files.ALTER_ADD) ||
                getFileType(fileName).equals(Files.ALTER_CHANGE) ||
                getFileType(fileName).equals(Files.ALTER_DROP)
        ){
            pattern = Pattern.compile("[to|from]_([a-z|_]+)_table");
        }else if(getFileType(fileName).equals(Files.CREATE)){
            pattern = Pattern.compile("create_([a-z|_]+)_table");
        }
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

}
