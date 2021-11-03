package com.cms.db;

import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.db.impl.CommonSqlDB;
import com.cms.db.impl.MongoDB;
import com.cms.db.impl.TestDB;

public class DbFactory {

    private CommonDB DB;
    private String dbType;

    public DbFactory(){
        create(this.getDbType());
    }

    public DbFactory(String dbType){
        this.dbType = dbType;
        create(dbType);
    }

    public String getDbType(){
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();
        this.dbType = configuration.getDbType();
        return this.dbType;
    }

    public void create(String type){
        if(type.equals("sql")){
            this.DB = new CommonSqlDB();
        }else if(type.equals("mongo")) {
            this.DB = new MongoDB();
        }else if(type.equals("test")){
            this.DB = new TestDB();
        }else{
            System.out.println("No matching DB implementation. Ple check applicationConfig.json.");
            System.exit(1);
        }
    }

    public CommonDB getDbInstance(){
        if(this.DB == null){
            create(this.getDbType());
        }
        return this.DB;
    }
}
