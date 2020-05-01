
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
//        QueryBuilder queryBuilder = new QueryBuilder("users", null);
//        Parser p = new Parser("database");
//
//
//
//        Table table = new Table();
//        table.setTableName("migrations");
//        List<Column> columns = new ArrayList<Column>();
//        Column column = new Column();
//
//        column.setColumn_name("id");
//        column.setDatatype("biginteger");
//        column.setUnsigned(true);
//        column.setIs_primary_key(true);
//        column.setNot_null(true);
//
//        Column column1 = new Column();
//
//        column1.setColumn_name("name");
//        column1.setDatatype("string");
//
//        Column migrationName = new Column();
//
//        migrationName.setColumn_name("name");
//        migrationName.setDatatype("string");
//        migrationName.setNot_null(true);
//        migrationName.setIs_primary_key(true);
//
//        Column c = new Column();
//
//        c.setColumn_name("hello");
//        c.setDatatype("int");
//        columns.add(column1);
//        columns.add(c);
//        columns.add(column);
//        columns.add(migrationName);
//        table.setColumns(columns);
////        columns = new ArrayList<>();
////
////        Column c = new Column();
////
////        c.setColumn_name("hello");
////        c.setDatatype("string");
////        columns.add(column1);
////        columns.add(c);
////        table.setAlterColumns(columns);
////        System.out.println(table.getColumns());
//        System.out.println(new SchemaBuilder().generateTableQuery(table));
//        Parser p = new Parser("database");
//        System.out.println(p.getTables());
//        System.out.println(Helper.getFileType("2020_04_06_054904_add_sd_table_answers.json "));
    }

}
