package com.cms.services;


import com.cms.db.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonServiceIml implements CommonService {

    protected CommonDB DB;
    public CommonServiceIml() {
        this.DB = new DbFactory().getDbInstance();
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
            return null;
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

    @Override
    public Map<String, Map<String, Object>> getTableMetaData(String type) throws Exception {
        return this.DB.getTableMetaData(type);
    }
}
