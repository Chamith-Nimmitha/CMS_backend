package com.cms.services;


import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.db.CommonDB;
import com.cms.db.CommonSqlDB;
import com.cms.db.MongoDB;
import com.cms.db.TestDB;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonServiceIml implements CommonService, BasicService{

    protected static CommonDB DB;
    public CommonServiceIml() {
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();
        String dbType = configuration.getDbType();
        System.out.println(dbType);
        if(dbType.equals("sql")){
            this.DB = new CommonSqlDB();
        }else if(dbType.equals("mongo")) {
            this.DB = new MongoDB();
        }else if(dbType.equals("test")){
            this.DB = new TestDB();
        }else{
            System.out.println("No matching DB implementation. Ple check applicationConfig.json.");
        }
    }
    public CommonServiceIml(CommonDB DB){
        this.DB = DB;
    }

    /*
    * get all data from any table
    * */
    @Override
    public List getAll(String type) {
        List res = null;
        try {
            res = this.DB.select(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.forEach(System.out::println);
        return  res;
    }

    /*
    * delete specific data from any table
    * */
    @Override
    public boolean delete(String type, String pk) {
        try {
            int result = this.DB.delete(type, pk);
            if(result == 1){
                System.out.println("Deleted.");
                return true;
            }else{
                throw new Exception("Deletion failed.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /*
    * add new data to any table
    * */
    @Override
    public boolean add(String type, Map<String, Object> data) {
        try {
            this.DB.insert(type, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
    * update any record in any table
    * */
    @Override
    public boolean update(String type, Map<String, Object> newData, Map<String, String> where) {
        try {
            Map<String, Object> data = new HashMap<>();
            return this.DB.update(type, newData, where);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * get all data from any table which match the pk
     * */
    @Override
    public Map<String, Object> getOne(String type, String pk) {
        Map<String, String> where = new HashMap<>();
        where.put("id", pk);

        List result = null;
        try {
            result = this.DB.select(type, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!result.isEmpty()){
            return (Map) result.get(0);
        }
        return  new HashMap();
    }
}
