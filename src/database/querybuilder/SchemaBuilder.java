package database.querybuilder;

import constants.DefaultLength;
import database.Column;
import database.Table;

public class SchemaBuilder {

    public SchemaBuilder(){
        columnBuilder = new ColumnBuilder();
    }

    public String generateTableQuery(Table table){
        String query = "CREATE TABLE " + table.getTableName() + "(\n";
        String primaryKey = "\nPRIMARY KEY (";
        String foreignKey = "";
        for(Column column : table.getColumns()){

            String columnDatatype = column.getDatatype();

            if(
                    columnDatatype.equalsIgnoreCase("int") ||
                    columnDatatype.equalsIgnoreCase("tinyint") ||
                    columnDatatype.equalsIgnoreCase("biginteger") ||
                    columnDatatype.equalsIgnoreCase("bigint")
            ){
                query += columnBuilder.getColumnQueryForNumber(column);
                primaryKey += columnBuilder.addColumnToPrimaryKey(column);
            }
            else if(
                    columnDatatype.equalsIgnoreCase("string") ||
                    columnDatatype.equalsIgnoreCase("varchar")
            ){
                query += columnBuilder.getColumnQueryForString(column);
                primaryKey += columnBuilder.addColumnToPrimaryKey(column);
            }
//            else if(columnDatatype.equalsIgnoreCase("string") || columnDatatype.equalsIgnoreCase("varchar")){
//
//                query += column.getColumn_name() +
//                        " VARCHAR" +
//                        addLengthAttribute(column) +
//                        addDefaultAttribute(column) +
//                        ",\n";
//
//                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";
//
//            }else if(columnDatatype.equalsIgnoreCase("text")){
//
//                query += column.getColumn_name() +
//                        " TEXT"  +
//                        ",\n";
//
//                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";
//
//            }else if(columnDatatype.equalsIgnoreCase("timestamp")){
//
//                query += column.getColumn_name() +
//                        " TIMESTAMP"  +
//                        addDefaultAttribute(column) +
//                        ",\n";
//
//                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";
//
//            }
//
//            if(column.isForeignKey()){
//                foreignKey += addForeignKeyAttributes(query, column) + "\n";
//            }
        }
        query = query.substring(0,query.length()-2) + "," + primaryKey.substring(0, primaryKey.length()-2) + "),";

        if(!foreignKey.equals("")){
            query += "\n" + foreignKey;
        }else{
            query = query.substring(0,query.length()-1);
        }
        query +=  "\n);";
        return query;

    }

    private ColumnBuilder columnBuilder = new ColumnBuilder();
}
