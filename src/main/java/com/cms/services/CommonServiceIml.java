package com.cms.services;


import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.db.CommonDB;
import com.cms.db.CommonSqlDB;
import com.cms.db.MongoDB;

import java.io.IOException;

public class CommonServiceIml implements CommonService{

    protected static CommonDB DB;
    public CommonServiceIml() {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        Configuration configuration = null;
        try {
            configuration = configurationManager
                    .loadConfiguration("src/main/resources/applicationConfig.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Configuration manager failed in CommonServiceIml");
            System.exit(1);
        }
        String dbType = configuration.getDbType();
        System.out.println(dbType);
        if(dbType.equals("sql")){
            this.DB = new CommonSqlDB();
        }else if(dbType.equals("mongo")){
            this.DB = new MongoDB();
        }else{
            System.out.println("No matching DB implementation. Ple check applicationConfig.json.");
        }
    }
    public CommonServiceIml(CommonDB DB){
        this.DB = DB;
    }
}
