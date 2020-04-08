package database.querybuilder;

import constants.DefaultLength;
import database.Column;
import database.Table;
import parser.Parser;

public class QueryBuilder implements DefaultLength {

    public boolean generateTable(){

        Table table = new Parser("").getTable();
//        System.out.println(table);
        String query = "CREATE TABLE " + table.getTableName() + "( ";

        for(Column column : table.getColumns()){

            String columnDatatype = column.getDatatype();

            if(columnDatatype.equalsIgnoreCase("int")){
                query += column.getColumn_name() + " INT" + addLengthAttribute(column) +  addUnsignedAttribute(column) + addPrimaryKeyAttribute(column) + ",\n";
            }else if(columnDatatype.equalsIgnoreCase("biginteger") || columnDatatype.equalsIgnoreCase("bigint")){
                query += column.getColumn_name() + " BIGINT" +  addLengthAttribute(column) + addPrimaryKeyAttribute(column) + ",\n";
            }else if(columnDatatype.equalsIgnoreCase("string") || columnDatatype.equalsIgnoreCase("varchar")){
                query += column.getColumn_name() + " VARCHAR" + addLengthAttribute(column) + addPrimaryKeyAttribute(column)  + ",\n";
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

        if(column.getDatatype().equalsIgnoreCase("string") && ((column.getLength() == 0))){
            return "(" + DefaultLength.varcharLength + ")";
        }else if (column.getLength() != 0){
            return "(" + column.getLength() + ")";
        }else if(column.getLength() == 0){
            return "";
        }
        return "";
    }

    public String addPrimaryKeyAttribute(Column column){
        return ((column.isPrimarykey()) ? ((column.getDatatype().equalsIgnoreCase("int") ||
                                            column.getDatatype().equalsIgnoreCase("biginteger")) ?
                                            " PRIMARY KEY AUTO_INCREMENT" : " PRIMARY KEY")
                : "");
    }

    public String addUnsignedAttribute(Column column){

        if(column.isUnsigned()){
            return " UNSIGNED";
        }
        return "";
    }

}
