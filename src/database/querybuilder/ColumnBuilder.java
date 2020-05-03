package database.querybuilder;

import constants.DefaultLength;
import database.Column;

import java.util.HashMap;
import java.util.Map;

public class ColumnBuilder {

    ColumnBuilder(){
        initDatatypeMapping();
    }

    private void initDatatypeMapping(){
        this.datatypeMapping = new HashMap<String, String>();
        this.datatypeMapping.put("int", "int");
        this.datatypeMapping.put("integer", "int");
        this.datatypeMapping.put("bigint", "bigint");
        this.datatypeMapping.put("biginteger", "bigint");
        this.datatypeMapping.put("tinyint", "tinyint");
        this.datatypeMapping.put("tinyinteger", "tinyint");
        this.datatypeMapping.put("string", "varchar");
        this.datatypeMapping.put("varchar", "varchar");
        this.datatypeMapping.put("timestamp", "timestamp");
        this.datatypeMapping.put("datetime", "datetime");
    }

    public String getColumnDatatype(Column column){
        return this.datatypeMapping.get(column.getDatatype()).toUpperCase();
    }

    public String getColumnQueryForNumber(Column column){
        return column.getColumn_name() + " " +
                getColumnDatatype(column) +
                getLengthAttribute(column) +
                getUnsignedAttribute(column) +
                getAutoIncrementAttribute(column) +
                getDefaultAttribute(column) +
                ",\n";
    }

    public String getColumnQueryForString(Column column){
        return column.getColumn_name() + " " +
                getColumnDatatype(column) +
                getLengthAttribute(column) +
                getDefaultAttribute(column) +
                ",\n";
    }

    public String getColumnQueryForDateAndTime(Column column){
        return column.getColumn_name() + " " +
                getColumnDatatype(column) +
                getDefaultAttribute(column) +
                ",\n";
    }

    public String getAutoIncrementAttribute(Column column){
        return (column.isPrimarykey() ? " AUTO_INCREMENT" : "");
    }

    public String getLengthAttribute(Column column){
        if(column.getDatatype().equalsIgnoreCase("string") && ((column.getLength() == 0))){
            return "(" + DefaultLength.varcharLength + ")";
        }else if (column.getLength() != 0){
            return "(" + column.getLength() + ")";
        }else if(column.getLength() == 0){
            return "";
        }
        return "";
    }

    public String getUnsignedAttribute(Column column){

        if(column.isUnsigned()){
            return " UNSIGNED";
        }
        return "";
    }

    public String getDefaultAttribute(Column column) {
        if(column.getDatatype().equalsIgnoreCase("timestamp") || column.getDatatype().equalsIgnoreCase("varchar")){
            return " DEFAULT " + column.getDefaultValue().toString();
        }
        if(null != column.getDefaultValue())
            return " DEFAULT " + column.getDefaultValue();
        return "";
    }

    public String addColumnToPrimaryKey(Column column){
        return (column.isPrimarykey() ? column.getColumn_name() + ", " : "");
    }

    public String getForeignKeyAttributes(Column column) {
        return "CONSTRAINT FOREIGN KEY (" +
                column.getColumn_name() + ")" +
                " REFERENCES " + column.getForeignKeyAttributes().get("on_table") +
                "(" +  column.getForeignKeyAttributes().get("references") + ")" +
                (column.getForeignKeyAttributes().get("on_delete") != null ? " ON DELETE " + column.getForeignKeyAttributes().get("on_delete") : "");

    }

    private Map<String, String> datatypeMapping = null;

}
