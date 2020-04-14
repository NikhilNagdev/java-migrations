package database.querybuilder;

import constants.DefaultLength;
import database.CRUD;
import database.Column;
import database.Table;
import parser.Parser;

import java.sql.Connection;

public class QueryBuilder implements DefaultLength {

    public QueryBuilder(String tableName, CRUD crud){
        this.table = tableName;
        this.crud = crud;
    }

    public String generateTableQuery(Table table){

//        System.out.println(table);
        String query = "CREATE TABLE " + table.getTableName() + "( ";
        String primaryKey = "\nPRIMARY KEY (";
        String foreignKey = "";
        for(Column column : table.getColumns()){

            String columnDatatype = column.getDatatype();

            if(columnDatatype.equalsIgnoreCase("int")){
                query += column.getColumn_name() +
                        " INT" +
                        addLengthAttribute(column) +
                        addUnsignedAttribute(column) +
                        (column.isPrimarykey() ? " AUTO_INCREMENT " : "")
                        + addDefaultAttribute(column)
                        + ",\n";

                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";

            }else if(columnDatatype.equalsIgnoreCase("biginteger") || columnDatatype.equalsIgnoreCase("bigint")){

                query += column.getColumn_name() +
                        " BIGINT" +
                        addLengthAttribute(column) +
                        addUnsignedAttribute(column) +
                        (column.isPrimarykey() ? " AUTO INCREMENT " : "") +
                        addDefaultAttribute(column) +
                        ",\n";

                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", ": "";

            }else if(columnDatatype.equalsIgnoreCase("string") || columnDatatype.equalsIgnoreCase("varchar")){

                query += column.getColumn_name() +
                        " VARCHAR" +
                        addLengthAttribute(column) +
                        addDefaultAttribute(column) +
                        ",\n";

                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";

            }else if(columnDatatype.equalsIgnoreCase("text")){

                query += column.getColumn_name() +
                        " TEXT"  +
                        ",\n";

                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";

            }else if(columnDatatype.equalsIgnoreCase("timestamp")){

                query += column.getColumn_name() +
                        " TIMESTAMP"  +
                        addDefaultAttribute(column) +
                        ",\n";

                primaryKey += column.isPrimarykey() ? column.getColumn_name() + ", " : "";

            }

            if(column.isForeignKey()){
                foreignKey += addForeignKeyAttributes(query, column) + "\n";
            }


        }

//        primaryKey += ")";
        query = query.substring(0,query.length()-2) + "," + primaryKey.substring(0, primaryKey.length()-2) + "),";

        if(!foreignKey.equals("")){
            query += "\n" + foreignKey;
        }else{
            query = query.substring(0,query.length()-1);
        }
        query +=  ");";

//        System.out.println(query);
        return query;

    }

    private String addForeignKeyAttributes(String query, Column column) {

//        CONSTRAINT FOREIGN KEY (PersonID)
//        REFERENCES Persons(id)
//                ON DELETE CASCADE,
        String foreignKey = "CONSTRAINT FOREIGN KEY (" + column.getColumn_name() + ")" +
                " REFERENCES " + column.getForeignKeyAttributes().get("on_table") + "(" + column.getForeignKeyAttributes().get("references") + ")" +
                (column.getForeignKeyAttributes().get("on_delete") != null ? " ON_DELETE " + column.getForeignKeyAttributes().get("on_delete") : "");
        System.out.println("=================================================");
        System.out.println(foreignKey);
        System.out.println("=================================================");
        return foreignKey;

    }

    private String addDefaultAttribute(Column column) {

        if(column.getDatatype().equalsIgnoreCase("timestamp") || column.getDatatype().equalsIgnoreCase("varchar")){
            return " DEFAULT " + column.getDefaultValue().toString();
        }
        if(null != column.getDefaultValue())
            return " DEFAULT " + column.getDefaultValue();
        return "";
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

    public QueryBuilder select(String ...columnNames){



        return null;
    }

    private String table = "";
    private CRUD crud = null;

}
