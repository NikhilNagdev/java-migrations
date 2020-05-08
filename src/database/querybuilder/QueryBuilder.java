package database.querybuilder;

import constants.DefaultLength;
import constants.Files;
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
        this.joins = new ArrayList<Map<String, String>>();
        this.table = tableName;
        this.crud = crud;
        this.bindings = new ArrayList<String>();
    }

    /**
     * This method is used to take the columns which are to be selected
     * @param columnNames
     * @return QueryBuilder
     */
    public QueryBuilder select(String ...columnNames){
        for(String column : columnNames){
            this.columns.add(column);
        }
//        this.columns.addAll(Arrays.asList(columnNames));
        return this;
    }


    /**
     * This method clears all the things which were used to run the query.
     * This method is called after the query is run
     */
    public void clear(){
        this.whereMap.clear();
        this.bindings.clear();
    }


    /**
     * This methods runs the select query and returns the results.
     * @return Collection of results returned by the select query
     */
    public List<LinkedHashMap<String, Object>> get(){
        this.compileSelect();
        List<LinkedHashMap<String, Object>> result = this.crud.runSelect(this.compileSelect(), this.bindings);
        clear();//clearing all the bindings
        return result;
    }

    /**
     * This method is used to generate a select query with all the things used with it.
     * @return the generated selected story
     */
    public String compileSelect(){
        String query = "SELECT ";

        //Adding Columns to the query
        for(String column : this.columns){
            query += column + ", ";
        }

        query = query.substring(0,query.length()-2)
                + " FROM "
                + this.table
                + this.getJoinClause()
                + this.getWhereClause();
        return query;

    }


    /**
     * This methods generates the join clause for the select query
     * @return join clause
     */
    private String getJoinClause(){
        String joinClause = "";
        if(!joins.isEmpty()){
            for(Map<String, String> map : this.joins){
                if(map.containsKey("joinType")){
                    joinClause += " " + map.get("joinType") + " JOIN ON " + map.get("onTable") + " " + map.get("column1") + " " + map.get("operator") + " " + map.get("column2");
                }else{
                    joinClause += " JOIN " + map.get("onTable") + " ON " + map.get("column1") + " " + map.get("operator") + " " + map.get("column2");
                }
            }
        }
        return joinClause;
    }

    /**
     * This methods generates the where clause the query
     * @return where clause
     */
    private String getWhereClause(){
        String whereClause = " WHERE ";
        if(!whereMap.isEmpty()) {
            for (Map<String, String> map : this.whereMap) {
                if (map.containsKey("whereConditionalOperator") && map.get("whereConditionalOperator").equalsIgnoreCase("and")) {
                    whereClause += " AND " + map.get("column") + map.get("operator") + map.get("value");
                } else if (map.containsKey("whereConditionalOperator") && map.get("whereConditionalOperator").equalsIgnoreCase("or")) {
                    whereClause += " OR " + map.get("column") + map.get("operator") + map.get("value");
                } else {
                    whereClause += map.get("column") + map.get("operator") + map.get("value");
                }
            }
        }else{
            whereClause += "1";
        }
        return whereClause;
    }

    /**
     * This method is used to add a where clause to a query.
     * @param column column on which operation has to be performed
     * @param operator operator between the value and the column
     * @param value Value to be matched with the column
     * @return QueryBuilder
     */
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

    /**
     * This method is used to add a AND where clause to a query.
     * @param column column on which operation has to be performed
     * @param operator operator between the value and the column
     * @param value Value to be matched with the column
     * @return QueryBuilder
     */
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

    /**
     * This method is used to add a OR where clause to a query.
     * @param column column on which operation has to be performed
     * @param operator operator between the value and the column
     * @param value Value to be matched with the column
     * @return QueryBuilder
     */
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

    /**
     * This method is used to add a where clause to a query.
     * The default operator is "=" between column and the value
     * @param column column on which operation has to be performed
     * @param value Value to be matched with the column
     * @return QueryBuilder
     */
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

    /**
     * This method is used to add a AND where clause to a query.
     * The default operator is "=" between column and the value
     * @param column column on which operation has to be performed
     * @param value Value to be matched with the column
     * @return QueryBuilder
     */
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

    /**
     * This method is used to add a OR where clause to a query.
     * The default operator is "=" between column and the value
     * @param column column on which operation has to be performed
     * @param value Value to be matched with the column
     * @return QueryBuilder
     */
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


    /**
     * This method is used to add a join clause to the select query
     * @param onTable the table to join on
     * @param column1 the column1 for column condition
     * @param operator operator between two columns
     * @param column2 the column1 for column condition
     * @return QueryBuilder
     */
    public QueryBuilder join(String onTable, String column1, String operator, String column2){
        Map<String, String> joinMap = new HashMap<String, String>();
        joinMap.put("onTable", onTable);
        joinMap.put("column1", column1);
        joinMap.put("operator", operator);
        joinMap.put("column2", column2);
        this.joins.add(joinMap);
        return this;
    }

    /**
     * This method is used to add a left join clause to the select query
     * @param onTable the table to join on
     * @param column1 the column1 for column condition
     * @param operator operator between two columns
     * @param column2 the column1 for column condition
     * @return QueryBuilder
     */
    public QueryBuilder leftJoin(String onTable, String column1, String operator, String column2){
        Map<String, String> joinMap = new HashMap<String, String>();
        joinMap.put("joinType", "LEFT");
        joinMap.put("onTable", onTable);
        joinMap.put("column1", column1);
        joinMap.put("operator", operator);
        joinMap.put("column2", column2);
        this.joins.add(joinMap);
        return this;
    }

    /**
     * This method is used to add a right join clause to the select query
     * @param onTable the table to join on
     * @param column1 the column1 for column condition
     * @param operator operator between two columns
     * @param column2 the column1 for column condition
     * @return QueryBuilder
     */
    public QueryBuilder rightJoin(String onTable, String column1, String operator, String column2){
        Map<String, String> joinMap = new HashMap<String, String>();
        joinMap.put("jointType", "right");
        joinMap.put("onTable", onTable);
        joinMap.put("column1", column1);
        joinMap.put("operator", operator);
        joinMap.put("column2", column2);
        this.joins.add(joinMap);
        return this;
    }

    /**
     * This method is used to add a join clause to the select query.
     * Here the operator between columns is by defualt "="
     * @param onTable the table to join on
     * @param column1 the column1 for column condition
     * @param column2 the column1 for column condition
     * @return QueryBuilder
     */
    public QueryBuilder join(String onTable, String column1, String column2){
        Map<String, String> joinMap = new HashMap<String, String>();
        joinMap.put("onTable", onTable);
        joinMap.put("column1", column1);
        joinMap.put("operator", "=");
        joinMap.put("column2", column2);
        this.joins.add(joinMap);
        return this;
    }

    /**
     * This method is used to add a left join clause to the select query.
     * Here the operator between columns is by defualt "="
     * @param onTable the table to join on
     * @param column1 the column1 for column condition
     * @param column2 the column1 for column condition
     * @return QueryBuilder
     */
    public QueryBuilder leftJoin(String onTable, String column1, String column2){
        Map<String, String> joinMap = new HashMap<String, String>();
        joinMap.put("joinType", "LEFT");
        joinMap.put("onTable", onTable);
        joinMap.put("column1", column1);
        joinMap.put("operator", "=");
        joinMap.put("column2", column2);
        this.joins.add(joinMap);
        return this;
    }

    /**
     * This method is used to add a right join clause to the select query.
     * Here the operator between columns is by defualt "="
     * @param onTable the table to join on
     * @param column1 the column1 for column condition
     * @param column2 the column1 for column condition
     * @return QueryBuilder
     */
    public QueryBuilder rightJoin(String onTable, String column1, String column2){
        Map<String, String> joinMap = new HashMap<String, String>();
        joinMap.put("jointType", "right");
        joinMap.put("onTable", onTable);
        joinMap.put("column1", column1);
        joinMap.put("operator", "=");
        joinMap.put("column2", column2);
        this.joins.add(joinMap);
        return this;
    }

    public boolean insert(List<LinkedHashMap<String, Object>> values){
        List<List<Object>> bindings = new ArrayList<>();
        for(LinkedHashMap<String, Object> value : values){
            List<Object> binding = new ArrayList<>();
            for(String column : value.keySet()){
                binding.add(value.get(column));
            }
            bindings.add(binding);
        }
        return this.crud.runInsert(this.compileInsert(values), bindings);
    }

    private String compileInsert(List<LinkedHashMap<String, Object>> values) {
        String query = "INSERT INTO " + this.table + " ";
        String columns = "";
        String valuesInsert = " VALUES ";
        boolean areColumnIntialized = false;
        for(LinkedHashMap<String, Object> value : values){
            valuesInsert += "(";
            for(String column : value.keySet()){
                if(!areColumnIntialized)
                    columns += column + ", ";
                valuesInsert += "?, ";
            }
            valuesInsert = valuesInsert.substring(0, valuesInsert.length() - 2)  + "), ";
            areColumnIntialized = true;
        }
        query += "(" + columns.substring(0, columns.length() - 2) + ")"  + valuesInsert.substring(0, valuesInsert.length() - 2);
//        System.out.println(query);
        return query;
    }

    //Variable declarations
    private List<String> columns;
    private String table = "";
    private CRUD crud = null;
    private List<List<String>> wheres;
    private List<Map<String, String>> whereMap;
    private List<String> bindings = null;
    private List<Map<String, String>> joins = null;
}
