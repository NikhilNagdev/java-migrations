package database.querybuilder;

import constants.DefaultLength;
import database.CRUD;
import database.Column;
import database.Table;
import java.util.*;

public class QueryBuilder {

    public QueryBuilder(String tableName, CRUD crud){
        this.columns = new ArrayList<String>();
//        this.wheres = new TreeMap<String, String[]>();
        this.wheres = new ArrayList<List<String>>();
        this.whereMap = new ArrayList<Map<String, String>>();
        this.table = tableName;
        this.crud = crud;
        this.bindings = new ArrayList<String>();
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
                        (column.isPrimarykey() ? " AUTO_INCREMENT " : "") +
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
                (column.getForeignKeyAttributes().get("on_delete") != null ? " ON DELETE " + column.getForeignKeyAttributes().get("on_delete") : "");
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

        for(String column : columnNames){
            this.columns.add(column);
        }

//        this.columns.addAll(Arrays.asList(columnNames));

        return this;
    }


    public void clear(){
        this.whereMap.clear();
        this.bindings.clear();
    }

    public List<SortedMap<String, Object>> get(){
        List<SortedMap<String, Object>> result = this.crud.runSelect(this.compileSelect(), this.bindings);
        clear();
        return result;
    }

    public String compileSelect(){
        String query = "SELECT ";
        for(String column : this.columns){
            query += column + ", ";
        }
        String whereQuery = " WHERE ";
        for(Map<String, String> map : this.whereMap){
            if(map.containsKey("whereConditionalOperator") && map.get("whereConditionalOperator").equalsIgnoreCase("and")){
                whereQuery += " AND " + map.get("column") + map.get("operator") + map.get("value");
            }else if(map.containsKey("whereConditionalOperator") && map.get("whereConditionalOperator").equalsIgnoreCase("or")){
                whereQuery += " OR " + map.get("column") + map.get("operator") + map.get("value");
            }else{
                whereQuery += map.get("column") + map.get("operator") + map.get("value");
            }
        }
        query = query.substring(0,query.length()-2)  + " FROM " + this.table + whereQuery;
        return query;

    }

    public QueryBuilder where(String column, String operator, Object value){
//        String[] sarray = new String[2];
//        sarray[0] = column;
//        sarray[1] = operator;
//        this.wheres.put(column, sarray);
//        List<String> l = new ArrayList<String>();
//        l.add(column);
//        l.add(operator);
//        l.add(value+"");
        Map<String, String> m = new HashMap<String, String>();
        m.put("column", column);
        m.put("operator", operator);
        m.put("value", "?");
        this.whereMap.add(m);
        bindings.add(value+"");
//        this.wheres.add(l);
        return this;
    }

    public QueryBuilder andWhere(String column, String operator, Object value){
//        List<String> l = new ArrayList<String>();
//        l.add(column);
//        l.add(operator);
//        l.add(value+"");
//        this.wheres.add(l);
        Map<String, String> m = new HashMap<String, String>();
        m.put("column", column);
        m.put("operator", operator);
        m.put("value", "?");
        m.put("whereConditionalOperator", "and");
        bindings.add(value+"");
        this.whereMap.add(m);
        return this;
    }

    public QueryBuilder orWhere(String column, String operator, Object value){
//        List<String> l = new ArrayList<String>();
//        l.add(column);
//        l.add(operator);
//        l.add(value+"");
//        this.wheres.add(l);
        Map<String, String> m = new HashMap<String, String>();
        m.put("column", column);
        m.put("operator", operator);
        m.put("value", "?");
        m.put("whereConditionalOperator", "or");
        bindings.add(value+"");
        this.whereMap.add(m);
        return this;
    }

    public QueryBuilder where(String column, Object value){
//        String[] sarray = new String[2];
//        sarray[0] = column;
//        sarray[1] = operator;
//        this.wheres.put(column, sarray);
//        List<String> l = new ArrayList<String>();
//        l.add(column);
//        l.add(operator);
//        l.add(value+"");
        Map<String, String> m = new HashMap<String, String>();
        m.put("column", column);
        m.put("operator", "=");
        m.put("value", "?");
        this.whereMap.add(m);
        bindings.add(value+"");

//        this.wheres.add(l);
        return this;
    }

    public QueryBuilder andWhere(String column, Object value){
//        List<String> l = new ArrayList<String>();
//        l.add(column);
//        l.add(operator);
//        l.add(value+"");
//        this.wheres.add(l);
        Map<String, String> m = new HashMap<String, String>();
        m.put("column", column);
        m.put("operator", "=");
        m.put("value", "?");
        m.put("whereConditionalOperator", "and");
        bindings.add(value+"");
        this.whereMap.add(m);
        return this;
    }

    public QueryBuilder orWhere(String column, Object value){
//        List<String> l = new ArrayList<String>();
//        l.add(column);
//        l.add(operator);
//        l.add(value+"");
//        this.wheres.add(l);
        Map<String, String> m = new HashMap<String, String>();
        m.put("column", column);
        m.put("operator", "=");
        m.put("value", "?");
        m.put("whereConditionalOperator", "or");
        bindings.add(value+"");
        this.whereMap.add(m);
        return this;
    }

    private List<String> columns;
    private String table = "";
    private CRUD crud = null;
    private List<List<String>> wheres;
    private List<Map<String, String>> whereMap;
    private List<String> bindings = null;
//    private SortedMap<String, String[]> wheres;

}
