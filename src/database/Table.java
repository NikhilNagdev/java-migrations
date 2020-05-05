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

    /**
     * This method is used to remove columns that were dropped.
     * Removes Column objects from the Table object that were dropped
     */
    public void removeColumnsFromTableAfterDrop(){
        for(Column column : this.getAlterColumns()){
            this.columns.remove(column);
        }
    }

    /**
     * This method is used to change thw Column Object in Table Object.
     */
    public void addChangedColumnsToTableObj(){
        for(Column column : this.getAlterColumns()){
            //checking if the column is existing in Column list
            if(this.columns.contains(column)){
                //Changing the column with altered Column
                this.columns.set(this.columns.indexOf(column), column);
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
