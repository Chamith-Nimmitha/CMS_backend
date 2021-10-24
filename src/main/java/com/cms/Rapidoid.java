package com.cms;
import com.cms.services.UserService;
import com.cms.utils.Json;
import org.rapidoid.http.MediaType;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.setup.On;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rapidoid {


    public UserService getService(String type) throws Exception {
        if(type.equals("user")){
            return new UserService();
        }else{
            throw new Exception("Invalid content type.");
        }
    }

    public void runServer() {
        UserService ur = new UserService();
/*
//
////        get all users
//        On.get("/users").json((Req req) -> {
//            List results = null;
//            results = ur.getUserList();
//            Resp resp = req.response();
//            resp.header("Access-Control-Allow-Origin", "*");
//            resp.header("Access-Control-Allow-Headers", "*");
//            resp.json(results);
//            resp.code(200);
//            return resp;
//        });
//        On.page("/about").html("Hello World");
//        On.options("/users").json( (Req req) -> {
////            req.response().header("Content-type", "application/json; charset=utf-8");
//            req.response().header("Access-Control-Allow-Origin", "*");
//            req.response().header("Access-Control-Allow-Headers", "origin, x-requested-with, content-type, authorization, " +
//                    "xKey, xForceRoute");
//            req.response().header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, POST, DELETE, OPTIONS");
//            return req.response();
//        });
//
////        add new user
//        On.post("/user").json((Req req) -> {
//            Map<String, Object> reqData = req.data();
//            Map<Object, Object> data = new HashMap<>();
//            data.put("name", reqData.get("name"));
//            data.put("age", Integer.parseInt(reqData.get("age").toString()));
//            ur.insertUser(data);
//            Resp resp = req.response();
//            resp.json("{'msg': 'user creation success'}");
//            resp.code(201);
//            return resp;
//        });
//
////        delete a specific user
//        On.delete("/user/{pk}").json((Req req) -> {
//            ur.deleteUser(req.param("pk"));
//            Resp resp = req.response();
//            resp.json("{'msg': 'user creation success'}");
//            resp.code(204);
//            return resp;
//        });
//
////        get specific user
//        On.get("/user/{pk}").json((Req req) -> {
//
//            Map user = ur.getUser(req.param("pk"));
//            Resp resp = req.response();
//            resp.json(user);
//            resp.code(200);
//            return resp;
//        });
//
////        update user
//        On.patch("/user/{pk}").json((Req req) -> {
//            Map<String, String> where = new HashMap<>();
//            where.put("id", req.param("pk"));
//            boolean result = ur.updateUser(req.data(), where);
//            Resp resp = req.response();
//            if(result){
//                resp.code(200);
//                resp.json("{'msg':'User update success'}");
//            }else{
//                resp.code(400);
//                resp.json("{'error':'User update failed'}");
//            }
//            return resp;
//        });
//
*/

//        Genaral functions
        On.options("/users").json( (Req req) -> {
//            req.response().header("Content-type", "application/json; charset=utf-8");
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "origin, x-requested-with, content-type, authorization, " +
                    "xKey, xForceRoute");
            req.response().header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, POST, DELETE, OPTIONS");
            return req.response();
        });

        //        get all users
        On.get("/{type}s").json((Req req) -> {
            List results = null;
            System.out.println("called");

            UserService us = getService(req.param("type"));
            results = us.getUserList();
            Resp resp = req.response();
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.json(results);
            resp.code(200);
            return resp;
        });

//        add new user
        On.post("/{type}").json((Req req) -> {
            System.out.println("called");
            Map<String, Object> reqData = req.data();
            Map<Object, Object> data = new HashMap<>();
            data.put("name", reqData.get("name"));
            data.put("age", Integer.parseInt(reqData.get("age").toString()));
            ur.insertUser(data);
            Resp resp = req.response();
            resp.json("{'msg': 'user creation success'}");
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.code(201);
            return resp;
        });

//        delete a specific user
        On.delete("/{type}/{pk}").json((Req req) -> {
            ur.deleteUser(req.param("pk"));
            Resp resp = req.response();
            resp.json("{'msg': 'user creation success'}");
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.code(204);
            return resp;
        });

//        get specific user
        On.get("/{type}/{pk}").json((Req req) -> {

            Map user = ur.getUser(req.param("pk"));
            Resp resp = req.response();
            resp.json(user);
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.code(200);
            return resp;
        });

//        update user
        On.patch("/{type}/{pk}").json((Req req) -> {
            Map<String, String> where = new HashMap<>();
            where.put("id", req.param("pk"));
            boolean result = ur.updateUser(req.data(), where);
            Resp resp = req.response();
            if(result){
                resp.code(200);
                resp.json("{'msg':'User update success'}");
            }else{
                resp.code(400);
                resp.json("{'error':'User update failed'}");
            }
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.code(200);
            return resp;
        });
    }
}
