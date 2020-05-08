
import commands.CommandProcessor;
import database.Column;
import database.Table;
import database.querybuilder.QueryBuilder;
import database.querybuilder.SchemaBuilder;
import helper.Helper;
import parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        new CommandProcessor().processCommand(args);
    }

}
