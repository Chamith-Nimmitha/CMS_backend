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
        super();
    }
//    For dependency injection
    public UserService(CommonDB db){
        super(db);
    }


}
