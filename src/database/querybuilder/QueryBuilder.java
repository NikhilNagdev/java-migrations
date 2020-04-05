package database.querybuilder;

import database.Column;
import database.Table;
import parser.Parser;

public class QueryBuilder {

    public boolean generateTable(){

        Table table = new Parser("").getTable();
//        System.out.println(table);
        String query = "CREATE TABLE " + table.getTableName() + "( ";

        for(Column column : table.getColumns()){

            String columnDatatype = column.getDatatype();

            if(columnDatatype.equalsIgnoreCase("int")){
                query += column.getColumn_name() + " INT" +  addLengthAttribute(column) + adPrimaryKeyAttribute(column) + ",\n";
            }else if(columnDatatype.equalsIgnoreCase("biginteger") || columnDatatype.equalsIgnoreCase("bigint")){
                query += column.getColumn_name() + " BIGINT" +  addLengthAttribute(column) + adPrimaryKeyAttribute(column) + ",\n";
            }else if(columnDatatype.equalsIgnoreCase("string") || columnDatatype.equalsIgnoreCase("varchar")){
                query += column.getColumn_name() + " VARCHAR" + addLengthAttribute(column) + adPrimaryKeyAttribute(column)  + ",\n";
            }else if(columnDatatype.equalsIgnoreCase("text")){
                query += column.getColumn_name() + " TEXT"  + ",\n";
            }else if(columnDatatype.equalsIgnoreCase("timestamp")){
                query += column.getColumn_name() + " TIMESTAMP"  + ",\n";
            }


        }

        query = query.substring(0,query.length()-2) + ");";

        System.out.println(query);
        return true;

    }


    public String addLengthAttribute(Column column){
        return ((column.getLength() != 0) ? "(" + column.getLength() + ")" : "");
    }

    public String adPrimaryKeyAttribute(Column column){
        return ((column.isPrimarykey()) ? ((column.getDatatype().equalsIgnoreCase("int") ||
                                            column.getDatatype().equalsIgnoreCase("biginteger")) ?
                                            " PRIMARY KEY AUTO_INCREMENT" : " PRIMARY KEY")
                : "");
    }

}
