package com.cms.services;

import com.cms.db.CommonDB;
import com.cms.db.DbFactory;

import java.util.HashMap;
import java.util.Map;

public class DbSchemaManageService {

    protected CommonDB DB;
    public DbSchemaManageService() {
        this.DB = new DbFactory().getDbInstance();
    }

    public boolean createTableSchema(Map<String, Object> metaData) throws Exception {

        return this.DB.createTableSchema(metaData);
    }
}
