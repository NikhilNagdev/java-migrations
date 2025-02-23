package database;

import java.util.Map;

public class Column implements Comparable<Column> {

    public boolean isForeignKey() {
        return isForeignKey;
    }

    @Override
    public int compareTo(Column column) {
        return this.columnName.compareTo(column.columnName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Column column = (Column) obj;
//        System.out.println(this.column_name + " == " + column.column_name);
        return this.getColumnName().equals(column.getColumnName());
    }

    @Override
    public int hashCode() {
        return this.columnName.hashCode();
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

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setColumnName(String column_name) {
        this.columnName = column_name;
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
                "column_name='" + columnName + '\'' +
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

    private String columnName = null;
    private String datatype = null;
    private int length = 0;
    private boolean is_primary_key;
    private boolean not_null = true;
    private Object defaultValue = null;
    private boolean isUnsigned;
    private boolean isForeignKey;
    private Map<String, String> foreignKeyAttributes;

}
