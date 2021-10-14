package com.cms;

import com.cms.pojo.User;
import com.cms.services.UserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Works");
        UserService ur = new UserService();

        List userList = ur.getUserList();
        userList.forEach(System.out::println);
    }
}
