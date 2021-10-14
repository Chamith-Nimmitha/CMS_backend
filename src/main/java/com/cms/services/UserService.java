package com.cms.services;

import com.cms.db.CommonSqlDB;
import com.cms.pojo.User;

import java.util.List;
import java.util.Map;

public class UserService extends CommonServiceIml{


    public UserService(){
        super(new CommonSqlDB());
    }

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

    public void insertUser(Map<Object, Object> data){
        try {
            this.DB.insert("user", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
