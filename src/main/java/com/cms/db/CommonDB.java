package com.cms.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommonDB {

    Set<String> getTableNames() throws Exception;
    Map<String, Map<String, Object>> getTableMetaData(String table) throws Exception;
    boolean createTableSchema(Map<String, Object> metaData) throws Exception;
    List select(String table) throws Exception;
    List select(String table, Map<String, String> where) throws Exception;
    void insert(String table, Map<String, Object> data) throws Exception;
    int delete(String table, String pk) throws Exception;
    boolean update(String table, Map<String, Object> data, Map<String, String> where) throws Exception;

}
