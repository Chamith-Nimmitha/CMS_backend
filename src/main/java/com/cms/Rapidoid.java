package com.cms;
import com.cms.services.CommonServiceIml;
import com.cms.services.DbSchemaManageService;
import com.cms.services.UserService;
import com.cms.utils.Json;
import com.cms.wsocket.NewHandler;
import org.java_websocket.server.WebSocketServer;
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

//      Genaral functions

        CommonServiceIml us = new CommonServiceIml();
//        NewHandler newHandler= new NewHandler();
//        WebSocketServer serverSockeet = newHandler.getServerSocket();
//        serverSockeet.start();


        On.options("/{type}s").plain( (Req req) -> {
            System.out.println("options");
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            req.response().code(200);
            req.response().plain("");
            return req.response();
        });

        On.options("/a/meta/{type}s}").plain( (Req req) -> {
            System.out.println("dfdfd");
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


        // Get tabel column informations
        On.get("/{type}s/meta").json( (Req req) -> {
            System.out.println("called");
            Map<String, Map<String, Object>> results= null;
            Resp resp = req.response();
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.header("Access-Control-Allow-Methods", "*");

            try {
                results = us.getTableMetaData(req.param("type"));
                resp.json(results);
                resp.code(200);
            }catch (Exception e){
                Map<String, String> errMsg = new HashMap<>();
                errMsg.put("error", req.param("type")+ " schema not found.");
                resp.json(errMsg);
                resp.code(200);
            }
            return resp;
        });

        // Create table
        On.post("/schema").json( (Req req) -> {
            DbSchemaManageService dbSchemaManageService = new DbSchemaManageService();
            Resp resp = req.response();
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Headers", "*");
            resp.header("Access-Control-Allow-Methods", "*");

            try {
                System.out.println(req.data());
                boolean result =  dbSchemaManageService.createTableSchema((Map<String, Object>) req.data());
                if(result){
                    Map<String, String> msg = new HashMap<>();
                    msg.put("msg"," Schema create successfull");
                    resp.json(msg);
                    resp.code(200);
                }else{
                    throw new Exception();
                }
            }catch (Exception e){
                System.out.println(e);
                Map<String, String> msg = new HashMap<>();
                msg.put("error"," Schema create failed");
                resp.json(msg);
                resp.code(400);
            }
            return resp;
        });

        //  get all record in specific type
        On.get("/{type}s").json((Req req) -> {
            List results = null;
            Resp resp = req.response();
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");
            try {
                results = us.getAll(req.param("type"));
                resp.json(results);
                resp.code(200);
            }catch (Exception e){
                Map<String, String> errMsg = new HashMap<>();
                errMsg.put("error", req.param("type")+ " schema not found.");
                resp.json(errMsg);
                resp.code(200);
            }
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
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");

            try {
                boolean result =  us.add(req.param("type"), data);
                if(result){
                    Map<String, String> msg = new HashMap<>();
                    msg.put("msg", req.param("type")+ " creation success.");
                    resp.json(msg);
                    resp.code(201);
//                serverSockeet.broadcast(req.param("type")+ " added. Please reload the page.");
                }else{
                   throw new Exception();
                }
            }catch (Exception e){
                Map<String, String> errMsg = new HashMap<>();
                errMsg.put("error", req.param("type")+ " creation failed.");
                resp.json(errMsg);
                resp.code(400);
            }
            return resp;

        });

        // delete a specific user form any table
        On.delete("/{type}s/{pk}").json((Req req) -> {
            Resp resp = req.response();
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");

            try {
                boolean result = us.delete(req.param("type"), req.param("pk"));
                if(result){
                    Map<String, String> msg = new HashMap<>();
                    msg.put("msg", req.param("type")+ " deletion success.");
                    resp.json(msg);
                    resp.code(201);
//                serverSockeet.broadcast(req.param("type")+ " added. Please reload the page.");
                }else{
                    throw new Exception();
                }
            }catch (Exception e){
                Map<String, String> errMsg = new HashMap<>();
                errMsg.put("error", req.param("type")+ " deletion failed.");
                resp.json(errMsg);
                resp.code(400);
            }
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

            Resp resp = req.response();
            req.response().header("Access-Control-Allow-Origin", "*");
            req.response().header("Access-Control-Allow-Headers", "*");
            req.response().header("Access-Control-Allow-Methods", "*");

            try {
                boolean result = us.update(req.param("type"), data, where);
                if(result){
                    Map<String, String> msg = new HashMap<>();
                    msg.put("msg", req.param("type")+ " update success.");
                    resp.json(msg);
                    resp.code(201);
//                serverSockeet.broadcast(req.param("type")+ " added. Please reload the page.");
                }else{
                    throw new Exception();
                }
            }catch (Exception e){
                Map<String, String> errMsg = new HashMap<>();
                errMsg.put("error", req.param("type")+ " update failed.");
                resp.json(errMsg);
                resp.code(400);
            }
            return resp;
        });
    }
}
