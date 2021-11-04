package com.cms.db.impl;

import com.cms.db.CommonDB;

import java.sql.SQLException;
import java.util.*;

public class TestDB implements CommonDB {

    public static int userCount = 0;
    public static Map<String, Map<String, Object>> store = new HashMap<>();

    public TestDB(){
        Map<String, Object> schemas = new HashMap<>();
        store.put("schemas", schemas);
        createTableSchema("user");
    }

    @Override
    public Set<String> getTableNames() throws SQLException {
        Set<String> tableNames = store.keySet();
        tableNames.remove("schemas");
        return tableNames;
    }

    @Override
    public Map<String, Map<String, Object>> getTableMetaData(String table) throws Exception {

        Map<String, Object> schemas = store.get("schemas");
        System.out.println(schemas);
        System.out.println(schemas.containsKey(table));
        System.out.println(table);
        if(schemas.containsKey(table)){
            Map<String, Object> tableSchema = (Map<String, Object>)schemas.get(table);
            return (Map<String, Map<String, Object>>) tableSchema.get("columns");
        }else{
            throw new Exception("Scema not found.");
        }
    }

    @Override
    public boolean createTableSchema(Map<String, Object> metaData) throws Exception {
        Map<String, Object> schemas =store.get("schemas");
        String tableName = (String) metaData.get("tableName");
        List<Map<String, Object>> columns = (List<Map<String, Object>>) metaData.get("columns");

        Map<String, Map<String, Object>> dbColumns = new HashMap<>();

        if( !schemas.containsKey(tableName) ){
            Map<String, Object> schema = new HashMap<>();

            for (Map<String, Object> column: columns){
                String cName = column.get("name").toString();
                String cType = column.get("dataType").toString();
                boolean isPrimaryKey = Boolean.parseBoolean(column.getOrDefault("isPrimary", "0").toString());
                int length = Integer.parseInt(column.get("length").toString());
                Map<String, Object> dbColumn = new HashMap<>();
                dbColumn.put("name", cName);
                dbColumn.put("dataType", cType);
                dbColumn.put("isPrimary", isPrimaryKey);
                dbColumn.put("length", length);

                dbColumns.put(cName, dbColumn);
            }
            schema.put("columns", dbColumns);
            this.store.get("schemas").put(tableName, schema);
            TestDB.store.put(tableName, new HashMap<String, Object>());
            System.out.println(store);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List select(String table) throws Exception {
        Map <String, Object> tableMap = this.getTable(table);
        List<Map<String , Object>> data = new ArrayList();
        tableMap.entrySet().forEach( (k) -> {
            Map<String, Object> row = (Map<String, Object>) k.getValue();
            row.put("id", k.getKey());
            data.add(row);
        });
        return data;
    }

    @Override
    public List select(String table, Map<String, String> where) throws Exception {
        Map <String, Object> tableMap = this.getTable(table);
        List<Map<String , Object>> data = new ArrayList();
        tableMap.entrySet().forEach( (k) -> {

            if( where.get("id").toString().equals( k.getKey())){
                Map<String, Object> row = (Map<String, Object>) k.getValue();
                row.put("id", k.getKey());
                data.add(row);
            }
        });
        return data;
    }

    @Override
    public void insert(String table, Map<String, Object> data) throws Exception {
        Map <String, Object> tableData = this.getTable(table);
        userCount++;
        tableData.put(""+userCount, data);
    }

    @Override
    public int delete(String table, String pk) throws Exception {

        Map <String, Object> tableData = this.getTable(table);
        if (tableData.containsKey(pk)){
            tableData.remove(pk);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean update(String table, Map<String, Object> data, Map<String, String> where) throws Exception {
        Map <String, Object> tableData = this.getTable(table);

        if(!where.containsKey("id")){
            return false;
        }
        Map <String, Object> forUpdate = (HashMap<String, Object>) tableData.get(where.get("id"));
        for( String k: data.keySet()){
            forUpdate.put(k, data.get(k));
        }

        return true;
    }

    public Map<String, Object> getTable(String tableName) throws Exception {

        if (!TestDB.store.containsKey(tableName)) {
            System.out.println("Schema not found");
            throw new Exception("Not Found.");
        }
        return TestDB.store.get(tableName);
    }

    private void createTableSchema(String tableName){
        Map<String, Object> schemas =store.get("schemas");
        if( !schemas.containsKey(tableName) ){
            Map<String, Object> schema = new HashMap<>();
            Map<String, Object> columns = new HashMap<>();

//            column 1
            Map<String, Object> column = new HashMap<>();
            column.put("name", "name");
            column.put("dataType", "VARCHAR");
            columns.put("name", column);

//          column 2
            Map<String, Object> column2 = new HashMap<>();
            column2.put("name", "age");
            column2.put("dataType", "INT");
            columns.put("age", column2);

            schema.put("columns", columns);
            this.store.get("schemas").put(tableName, schema);
            TestDB.store.put(tableName, new HashMap<String, Object>());
        }
    }
}
