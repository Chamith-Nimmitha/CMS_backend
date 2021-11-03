package com.cms.db.impl;

import com.cms.db.CommonDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDB implements CommonDB {

    public static int userCount = 0;
    public static Map<String, Map<String, Object>> store = new HashMap<>();

    public TestDB(){
    }

    @Override
    public Map<String, Map<String, String>> getTableMetaData(String table) {
        return null;
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

    public Map<String, Object> getTable(String tableName) {

        if (!TestDB.store.containsKey(tableName)) {
            TestDB.store.put(tableName, new HashMap<String, Object>());
        }
        return TestDB.store.get(tableName);
    }
}
