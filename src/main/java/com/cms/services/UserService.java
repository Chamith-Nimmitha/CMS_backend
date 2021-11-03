package com.cms.services;

import com.cms.db.CommonDB;

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
