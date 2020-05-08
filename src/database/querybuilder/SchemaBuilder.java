package database.querybuilder;

import constants.Files;
import database.Column;
import database.Table;

import java.util.ArrayList;
import java.util.List;

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
    }

    public List<String> generateAlterTableQuery(Table table, String alterType){
        List<String> queries = new ArrayList<String>();
        String query = "";
        String foreignKey = "";
        for(Column column : table.getAlterColumns()){
            query = "ALTER TABLE " + table.getTableName();
            //if alterType is DROP then we have to just add DROP COLUMN column_name to query
            if(alterType.equals(Files.ALTER_DROP)){
                query += " DROP COLUMN " + column.getColumnName();
                queries.add(query);
            }else{
                if(alterType.equals(Files.ALTER_CHANGE)){
                    query += " MODIFY COLUMN ";
                }else if(alterType.equals(Files.ALTER_ADD)){
                    query += " ADD COLUMN ";
                }else if(alterType.equals(Files.ALTER_DROP)){
                    query += " DROP COLUMN " + column.getColumnName();
                }
                String columnDatatype = column.getDatatype();
                if(
                        columnDatatype.equalsIgnoreCase("int") ||
                        columnDatatype.equalsIgnoreCase("tinyint") ||
                        columnDatatype.equalsIgnoreCase("biginteger") ||
                        columnDatatype.equalsIgnoreCase("bigint")
                ){
                    query += columnBuilder.getColumnQueryForNumber(column);
                }else if(
                        columnDatatype.equalsIgnoreCase("string") ||
                                columnDatatype.equalsIgnoreCase("varchar")
                ){
                    query += columnBuilder.getColumnQueryForString(column);
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
                queries.add(query);
            }
        }
        return queries;
    }

    private ColumnBuilder columnBuilder = null;
}
