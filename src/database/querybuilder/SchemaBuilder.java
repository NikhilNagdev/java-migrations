package database.querybuilder;

import constants.DefaultLength;
import constants.Files;
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
//            else if(columnDatatype.equalsIgnoreCase("text")){
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
            if(column.isForeignKey()){
                foreignKey += columnBuilder.getForeignKeyAttributes(column) + "\n";
            }
        }
        query = query.substring(0,query.length()-2) + "," + primaryKey.substring(0, primaryKey.length()-2) + "),";

        if(!foreignKey.equals("")){
            query += "\n" + foreignKey;
        }else{
            query = query.substring(0,query.length()-1);
        }
        query +=  "\n);";
        return query;

//    query = query.substring(0,query.length()-2) + "," + primaryKey.substring(0, primaryKey.length()-2) + "),";
//
//        query += foreignKey.equals("") ? query.substring(0,query.length()-1) : "\n" + foreignKey;
//        query +=  "\n);";
//        return query;

    }

    public String generateAlterTableQuery(Table table, String alterType){
        String query = "";
//        String primaryKey = "PRIMARY KEY";
        String foreignKey = "";
        for(Column column : table.getAlterColumns()){
            query = "ALTER TABLE " + table.getTableName();
//            if(table.getColumns().contains(column)){
//                query += " MODIFY COLUMN ";
//            }else{
//                query += " ADD COLUMN ";
//            }
            if(alterType.equals(Files.ALTER_CHANGE)){
                query += " MODIFY COLUMN ";
            }else if(alterType.equals(Files.ALTER_ADD)){
                query += " ADD COLUMN ";
            }

            String columnDatatype = column.getDatatype();

            if(
                    columnDatatype.equalsIgnoreCase("int") ||
                    columnDatatype.equalsIgnoreCase("tinyint") ||
                    columnDatatype.equalsIgnoreCase("biginteger") ||
                    columnDatatype.equalsIgnoreCase("bigint")
            ){
                query += columnBuilder.getColumnQueryForNumber(column);
//                primaryKey += column.isPrimarykey() ? column.getColumn_name() : "";

            }else if(
                    columnDatatype.equalsIgnoreCase("string") ||
                    columnDatatype.equalsIgnoreCase("varchar")
            ){
                query += columnBuilder.getColumnQueryForString(column);
//                primaryKey += columnBuilder.addColumnToPrimaryKey(column);
            }
            /*else if(columnDatatype.equalsIgnoreCase("text")){

                query += column.getColumn_name() +
                        " TEXT"  +
                        ",\n";

//                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";

            }*/else if(columnDatatype.equalsIgnoreCase("timestamp")){
                query += columnBuilder.getColumnQueryForDateAndTime(column);
            }

            if(column.isForeignKey()){
                query += "ADD " + columnBuilder.getForeignKeyAttributes(column);
            }else{
                query = query.substring(0, query.length()-2);
            }
        }
        return query;
    }

    private ColumnBuilder columnBuilder = null;
}
