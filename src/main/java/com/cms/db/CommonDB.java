package com.cms.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CommonDB {

    void createConnection() throws Exception;
    CommonDB getDB();
    List select(String table) throws Exception;
    List select(String table, Map<String, String> where);
    void insert(String table, Map<Object, Object> data) throws Exception;
    int delete(String table, String pk) throws Exception;
    List update(String table, Map<String, String> data, Map<String, String> where);
}
