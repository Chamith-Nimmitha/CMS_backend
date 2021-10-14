package com.cms.services;

import com.cms.db.CommonDB;
import com.cms.db.CommonSqlDB;
import com.cms.pojo.User;

import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService extends CommonServiceIml{

//    default constructor
    public UserService(){
        super(new CommonSqlDB());
    }
//    For dependency injection
    public UserService(CommonDB db){
        super(db);
    }

    /**
     * get all users from user DB
     */
    public List getUserList(){
        List res = null;
        try {
            res = this.DB.select("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.forEach(System.out::println);
        return  res;
    }

   /**
    *insert new user to DB
    */
    public void insertUser(Map<Object, Object> data){
        try {
            this.DB.insert("user", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete a user from DB
     */
    public void deleteUser(String pk) {

        try {
            int result = this.DB.delete("user", pk);
            if(result == 1){
                System.out.println("Deleted.");
            }else{
                throw new Exception("Deletion failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a users in DB
     **/
    public boolean updateUser(Map<String,Object> newData, Map<String, String> where){
        try {
            return this.DB.update("user", newData, where);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map getUser(String pk){
        Map<String, String> where = new HashMap<>();
        where.put("id", pk);

        List result = null;
        try {
            result = this.DB.select("user", where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!result.isEmpty()){
            return (Map) result.get(0);
        }
        return  new HashMap();

    }
}
