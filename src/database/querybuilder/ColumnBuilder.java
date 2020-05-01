package database.querybuilder;

import constants.DefaultLength;
import database.Column;

public class ColumnBuilder {

    public String getColumnQueryForNumber(Column column){
        return column.getColumn_name() +
                column.getDatatype().toUpperCase() +
                getLengthAttribute(column) +
                addUnsignedAttribute(column) +
                addAutoIncrementAttribute(column) +
                addDefaultAttribute(column) +
                ",\n";
    }

    private String addAutoIncrementAttribute(Column column){
        return (column.isPrimarykey() ? " AUTO_INCREMENT " : "");
    }

    public String getColumnQueryForString(Column column){
        return "";
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

}
