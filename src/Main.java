
import commands.CommandProcessor;
import helper.Helper;
import parser.Parser;

public class Main {

    public static void main(String[] args) {
        new CommandProcessor().processCommand(args);
//        Parser p = new Parser("database");
//        System.out.println(p.getTables());
//        System.out.println(Helper.getFileType("2020_04_06_054904_add_sd_table_answers.json "));
    }

}
