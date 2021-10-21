package com.cms;

import com.cms.pojo.User;
import com.cms.services.UserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("check db");
        UserService ur = new UserService();

//        print all db users
        List userList = ur.getUserList();
        userList.forEach(System.out::println);

        System.out.println("start server");
        Rapidoid rd = new Rapidoid();
        rd.runServer();
        System.out.println("Working properly");
    }
}
