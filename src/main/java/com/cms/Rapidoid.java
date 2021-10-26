package com.cms;
import com.cms.services.CommonServiceIml;
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


    public void runServer() {
        On.address("127.0.0.1").port(9999);
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

        CommonServiceIml us = new CommonServiceIml();

        On.options("/{type}s").plain( (Req req) -> {
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            req.response().code(200);
            req.response().plain("");
            return req.response();
        });

        On.options("/{type}s/{pk}").plain( (Req req) -> {
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            req.response().code(200);
            req.response().plain("");
            return req.response();
        });

        //  get all record in specific type
        On.get("/{type}s").json((Req req) -> {
            List results = null;
            results = us.getAll(req.param("type"));
            Resp resp = req.response();
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            resp.json(results);
            resp.code(200);
            return resp;
        });

        // add new user to any table
        On.post("/{type}s").json((Req req) -> {
            Map<String, Object> reqData = req.data();
            Map<String, Object> data = new HashMap<>();
            for(String k: reqData.keySet()){
                if(k.equals("type")){
                    continue;
                }
                data.put(k, reqData.get(k));
            }
            Resp resp = req.response();
            boolean result =  us.add(req.param("type"), data);
            if(result){
                resp.json("{'msg': '"+req.param("type")+" creation success'}");
                resp.code(201);
            }else{
                resp.json("{'error': '"+req.param("type")+" creation success'}");
                resp.code(400);
            }
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            return resp;
        });

        // delete a specific user form any table
        On.delete("/{type}s/{pk}").json((Req req) -> {
            boolean result = us.delete(req.param("type"), req.param("pk"));
            Resp resp = req.response();
            if(result){
                resp.json("{'msg': '"+req.param("type")+" delete success'}");
                resp.code(204);
            }else{
                resp.json("{'error': '"+req.param("type")+" delete failed'}");
                resp.code(400);
            }
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            return resp;
        });

        // get specific user from any table
        On.get("/{type}s/{pk}").json((Req req) -> {
            Map user = us.getOne(req.param("type"), req.param("pk"));
            Resp resp = req.response();
            resp.json(user);
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            resp.code(200);
            return resp;
        });

        // update user in any table
        On.patch("/{type}s/{pk}").json((Req req) -> {
            Map<String, Object> reqData = req.data();
            Map<String, String> where = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            for(String k: reqData.keySet()){
                if(k.equals("type") || k.equals("pk")){
                    continue;
                }
                data.put(k, reqData.get(k));
            }
            where.put("id", req.param("pk"));
            boolean result = us.update(req.param("type"), data, where);
            Resp resp = req.response();
            if(result){
                resp.code(200);
                resp.json("{'msg':'"+req.param("type") +" update success'}");
            }else{
                resp.code(400);
                resp.json("{'error':'"+req.param("type") +" update failed'}");
            }
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            resp.code(200);
            return resp;
        });
    }
}
