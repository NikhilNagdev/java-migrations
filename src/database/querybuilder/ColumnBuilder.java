package database.querybuilder;

import constants.DefaultLength;
import database.Column;

import java.util.HashMap;
import java.util.Map;

public class ColumnBuilder {

    ColumnBuilder(){
        initDatatypeMapping();
    }

    /**
     * This method is used to initialize the datatypeMapping Map.
     */
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

    /**
     * This method is used to get the column name in uppercase
     * @param column Column obj from which column name has to be set
     * @return uppercased column name
     */
    public String getColumnDatatype(Column column){
        return this.datatypeMapping.get(column.getDatatype()).toUpperCase();
    }

    /**
     * This method is used to get the column query for number types of datatype.
     * @param column Column obj from which query has to be generated
     * @return column query
     */
    public String getColumnQueryForNumber(Column column){
        return column.getColumnName() + " " +
                getColumnDatatype(column) +
                getLengthAttribute(column) +
                getUnsignedAttribute(column) +
                getAutoIncrementAttribute(column) +
                getDefaultAttribute(column) +
                ",\n";
    }

    /**
     * This method is used to get the column query for string types of datatype.
     * @param column Column obj from which query has to be generated
     * @return column query
     */
    public String getColumnQueryForString(Column column){
        return column.getColumnName() + " " +
                getColumnDatatype(column) +
                getLengthAttribute(column) +
                getDefaultAttribute(column) +
                ",\n";
    }

    /**
     * This method is used to get the column query for data time types of datatype.
     * @param column Column obj from which query has to be generated
     * @return column query
     */
    public String getColumnQueryForDateAndTime(Column column){
        return column.getColumnName() + " " +
                getColumnDatatype(column) +
                getDefaultAttribute(column) +
                ",\n";
    }

    /**
     * This method is used to add AUTO_INCREMENT to column query if column is a primary key.
     * @param column Column obj in which primary key is to be checked
     * @return
     */
    public String getAutoIncrementAttribute(Column column){
        return (column.isPrimarykey() ? " AUTO_INCREMENT" : "");
    }

    /**
     * This method is used to add length attribute to a column in column query.
     * @param column Column obj from which length has to be taken
     * @return
     */
    public String getLengthAttribute(Column column){
        //getting the default length for string because string type of datatype needs a length
        if(column.getDatatype().equalsIgnoreCase("string") && ((column.getLength() == 0))){
            return "(" + DefaultLength.varcharLength + ")";
        }else if (column.getLength() != 0){
            return "(" + column.getLength() + ")";
        }else if(column.getLength() == 0){
            return "";
        }
        return "";
    }

    /**
     * This method is used to add unsigned attribute to a column in column query.
     * @param column Column obj to check if the unsigned attribute is there or not
     * @return
     */
    public String getUnsignedAttribute(Column column){
        if(column.isUnsigned()){
            return " UNSIGNED";
        }
        return "";
    }

    /**
     * This method is used to add default attribute to a column in column query.
     * @param column Column obj from which default value is to be get
     * @return
     */
    public String getDefaultAttribute(Column column) {
        //if datatype is timestamp or string type of datatype then we have convert the default Object value to String
        if(column.getDatatype().equalsIgnoreCase("timestamp") || column.getDatatype().equalsIgnoreCase("string")){
            return " DEFAULT " + column.getDefaultValue().toString();
        }
        //this is for datatype other than timestamp and string but we have to check if there is a default value for that column or not
        if(null != column.getDefaultValue())
            return " DEFAULT " + column.getDefaultValue();
        return "";
    }

    /**
     * This method generates a primary key query which is at the end of create query.
     * So if a column is primary key then the column name is added to primary key query
     * @param column Column obj to get name and check is the column is primary key
     * @return primary key query
     */
    public String addColumnToPrimaryKey(Column column){
        return (column.isPrimarykey() ? column.getColumnName() + ", " : "");
    }

    /**
     * This method generates a foreign key query which is at the end of create query or alter query.
     * So if a column is foreign key then the column's foreign key attributes are added to foreign key query
     * @param column Column obj to get column's foreign key attributes and check is the column is foreign key
     * @return foreign key query
     */
    public String getForeignKeyAttributes(Column column) {
        if(column.isForeignKey())
            return "CONSTRAINT FOREIGN KEY (" +
                column.getColumnName() + ")" +
                                 //foreignKetAttributes is a Map
                " REFERENCES " + column.getForeignKeyAttributes().get("on_table") +
                "(" +  column.getForeignKeyAttributes().get("references") + ")" +
                (column.getForeignKeyAttributes().get("on_delete") != null ? " ON DELETE " + column.getForeignKeyAttributes().get("on_delete") : "");
        else
            return "";
    }

    private Map<String, String> datatypeMapping = null;

}
