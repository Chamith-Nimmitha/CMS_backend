package com.cms.db;

import java.util.List;
import java.util.Map;

public interface CommonDB {

    void createConnection() throws Exception;
    CommonDB getDB();
    List select(String table) throws Exception;
    List select(String table, Map<String, String> where) throws Exception;
    void insert(String table, Map<String, Object> data) throws Exception;
    int delete(String table, String pk) throws Exception;
    boolean update(String table, Map<String, Object> data, Map<String, String> where) throws Exception;
}
