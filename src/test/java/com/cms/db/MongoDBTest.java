package com.cms.db;

import com.cms.db.impl.MongoDB;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class MongoDBTest {

    CommonDB DB = new MongoDB();
    @Test
    void select() {
        List users = null;
        try {
            users = DB.select("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.forEach(System.out::println);
    }

    @Test
    void testSelect() {
        List users = null;
        Map<String, String> where = new HashMap();
        where.put("name", "John");
        try {
            users = DB.select("user", where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.forEach(System.out::println);
    }

    @Test
    void insert() {
        Map<String, Object> userData = new HashMap();
        userData.put("name", "John");
        userData.put("age", "50");

        try {
            DB.insert("user", userData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void delete() {
        // insert new user
        Map<String, Object> userData = new HashMap();
        userData.put("name", "namal");
        userData.put("age", "53");

        try {
            DB.insert("user", userData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // select created user
        Map user = null;
        Map<String, String> where = new HashMap();
        where.put("name", "namal");
        try {
            user = (Map) DB.select("user", where).get(0);
        } catch (Exception e) {
            System.out.println("User not found");
            return;
        }
        int result = 0;
        try {
            result = DB.delete("user", user.get("_id").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    @Test
    void update() {
        // insert new user
        Map<String, Object> userData = new HashMap();
        userData.put("name", "Amal");
        userData.put("age", "53");

        try {
            DB.insert("user", userData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // select new user
        Map user = null;
        Map<String, String> where = new HashMap();
        where.put("name", "Amal");
        try {
            user = (Map) DB.select("user", where).get(0);
        } catch (Exception e) {
            System.out.println("User not found");
            return;
        }
        Map<String, String> whereUpdate = new HashMap();
        whereUpdate.put("_id", user.get("_id").toString());

        Map<String, Object> newData = new HashMap();
        newData.put("name", "asd");
        newData.put("age", "9999");

        boolean result = false;
        try {
            result = DB.update("user", newData, whereUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
//      delete created user
        try {
            DB.delete("user", user.get("_id").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result);
    }
}