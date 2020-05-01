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
    }

    public String getColumnDatatype(Column column){
        return this.datatypeMapping.get(column.getDatatype()).toUpperCase();
    }

    public String getColumnQueryForNumber(Column column){
        return column.getColumn_name() + " " +
                getColumnDatatype(column) +
                getLengthAttribute(column) +
                addUnsignedAttribute(column) +
                addAutoIncrementAttribute(column) +
                addDefaultAttribute(column) +
                ",\n";
    }

    public String getColumnQueryForString(Column column){
        return column.getColumn_name() + " " +
                getColumnDatatype(column) +
                getLengthAttribute(column) +
                addDefaultAttribute(column) +
                ",\n";
    }

    private String addAutoIncrementAttribute(Column column){
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

    public String addUnsignedAttribute(Column column){

        if(column.isUnsigned()){
            return " UNSIGNED";
        }
        return "";
    }

    private String addDefaultAttribute(Column column) {
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

    private Map<String, String> datatypeMapping = null;

}
