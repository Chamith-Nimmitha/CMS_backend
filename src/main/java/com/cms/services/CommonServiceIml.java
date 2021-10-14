package com.cms.services;


import com.cms.db.CommonDB;

public class CommonServiceIml implements CommonService{

    protected static CommonDB DB;
    public CommonServiceIml(CommonDB DB){
        this.DB = DB;
    }
}
