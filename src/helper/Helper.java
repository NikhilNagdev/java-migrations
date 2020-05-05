package helper;

import constants.Files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static boolean isMigrationTypeCreate(String name){
        return Pattern.compile("[0-9]{4}_([0-9]{2}_){2}[0-9]{6}_create_.*.json").matcher(name).find();
    }

    public static String getFileType(String name){
        Pattern pattern = Pattern.compile("[0-9]{4}_([0-9]{2}_){2}[0-9]{6}_([a-z]+)_.*.json");
        Matcher matcher = pattern.matcher(name);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(2);
        }
        if(result.equals("create")){
            return Files.CREATE;
        }else if(result.equals("add")){
//            System.out.println(result);
            return Files.ALTER_ADD;
        }else if(result.equals("change") || result.equals("modify")){
//            System.out.println(result);
            return Files.ALTER_CHANGE;
        }
//        else if(result.equals("add") || result.equals("change") || result.equals("modify")){
////            System.out.println(result);
//            return Files.ALTER;
//        }
        return "alter_drop";
    }

    public static String getTableNameFromFileName(String name){
        Pattern pattern = null;
        if(
                getFileType(name).equals(Files.ALTER_ADD) ||
                getFileType(name).equals(Files.ALTER_CHANGE) ||
                getFileType(name).equals(Files.ALTER_DROP)
        ){
            pattern = Pattern.compile("[to|from]_([a-z|_]+)_table");
        }else if(getFileType(name).equals(Files.CREATE)){
            pattern = Pattern.compile("create_([a-z|_]+)_table");
        }
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
