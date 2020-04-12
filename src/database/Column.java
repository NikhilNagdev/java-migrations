package database;

import java.util.Map;

public class Column {

    private String column_name = null;
    private String datatype = null;
    private int length = 0;
    private boolean is_primary_key;

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public void setIsForeignKey(boolean foreignKey) {
        isForeignKey = foreignKey;
    }

    public Map<String, String> getForeignKeyAttributes() {
        return foreignKeyAttributes;
    }

    public void setForeignKeyAttributes(Map<String, String> foreignKeyAttributes) {
        this.foreignKeyAttributes = foreignKeyAttributes;
    }

    private boolean not_null = true;
    private Object defaultValue = null;
    private boolean isUnsigned;
    private boolean isForeignKey;
    private Map<String, String> foreignKeyAttributes;

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
    }

    public String getColumn_name() {
        return column_name;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setColumnName(String column_name) {
        this.column_name = column_name;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setIs_primary_key(boolean is_primary_key) {
        this.is_primary_key = is_primary_key;
    }

    public void setNot_null(boolean not_null) {
        this.not_null = not_null;
    }

    public void setDefault_value(Object default_value) {
        this.defaultValue = default_value;
    }

    public int getLength() {
        return length;
    }

    public boolean isPrimarykey() {
        return is_primary_key;
    }

    public boolean isNot_null() {
        return not_null;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "Column{" +
                "column_name='" + column_name + '\'' +
                ", datatype='" + datatype + '\'' +
                ", length=" + length +
                ", is_primary_key=" + is_primary_key +
                ", not_null=" + not_null +
                ", defaultValue=" + defaultValue +
                ", isUnsigned=" + isUnsigned +
                ", isForeignKey=" + isForeignKey +
                ", foreignKeyAttributes=" + foreignKeyAttributes +
                '}';
    }
}
