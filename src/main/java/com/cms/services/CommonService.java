package com.cms.services;

import java.util.List;
import java.util.Map;

public interface CommonService {

    List getAll(String type);
    boolean delete(String type, String pk );
    boolean add(String type, Map<String, Object> data);
    boolean update(String type, Map<String, Object> newData, Map<String, String> where );
    Map<String, Object> getOne(String type, String pk);
    Map<String, Map<String, String>> getTableMetaData(String type);
}
