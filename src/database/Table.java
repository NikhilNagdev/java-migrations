package database;

import java.util.ArrayList;
import java.util.List;

public class Table {

    public Table(){
        columns = new ArrayList<Column>();
        alterColumns = new ArrayList<Column>();
    }

    private String tableName = null;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Column> getAlterColumns() {
        return alterColumns;
    }

    public void setAlterColumns(List<Column> alterColumns) {
        this.alterColumns = alterColumns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addAlteredColumnToTableObject(Column column) {
        this.getColumns().add(column);
    }

    public void removeColumnsFromTableAfterDrop(){
        for(Column column : this.getAlterColumns()){
            if(this.columns.contains(column)){
                this.columns.remove(column);
            }
        }

    }

    @Override
    public String toString() {
        return "Table{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", alterColumns=" + alterColumns +
                '}';
    }

    private List<Column> columns = null;
    private List<Column> alterColumns = null;

}
