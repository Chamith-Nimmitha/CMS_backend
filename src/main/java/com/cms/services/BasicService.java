package com.cms.services;

import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.db.CommonDB;
import com.cms.db.CommonSqlDB;
import com.cms.db.MongoDB;
import com.cms.db.TestDB;

import java.util.List;
import java.util.Map;

public interface BasicService extends CommonService{

    List getAll(String type);
    boolean delete(String type, String pk );
    boolean add(String type, Map<String, Object> data);
    boolean update(String type, Map<String, Object> newData, Map<String, String> where );
    Map<String, Object> getOne(String type, String pk);
}
