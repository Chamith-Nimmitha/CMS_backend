package com.cms.services;

import com.cms.db.CommonDB;
import com.cms.db.DbFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DbSchemaManageService {

    protected CommonDB DB;
    public DbSchemaManageService() {
        this.DB = new DbFactory().getDbInstance();
    }

    public boolean createTableSchema(Map<String, Object> metaData) throws Exception {

        return this.DB.createTableSchema(metaData);
    }

    public Set<String> getDbTableNames() throws Exception {
        return this.DB.getTableNames();
    }
}
