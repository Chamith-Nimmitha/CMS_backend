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

    public void runServer() {
        UserService ur = new UserService();

//        get all users
        On.get("/users").json((Req req) -> {
            List results = null;
            results = ur.getUserList();
            Resp resp = req.response();
            resp.json(results);
            resp.code(200);
            return resp;
        });
        On.page("/about").html("Hello World");

//        add new user
        On.post("/user").json((Req req) -> {
            Map<String, Object> reqData = req.data();
            Map<Object, Object> data = new HashMap<>();
            data.put("name", reqData.get("name"));
            data.put("age", Integer.parseInt(reqData.get("age").toString()));
            ur.insertUser(data);
            Resp resp = req.response();
            resp.json("{'msg': 'user creation success'}");
            resp.code(201);
            return resp;
        });

//        delete a specific user
        On.delete("/user/{pk}").json((Req req) -> {
            ur.deleteUser(req.param("pk"));
            Resp resp = req.response();
            resp.json("{'msg': 'user creation success'}");
            resp.code(204);
            return resp;
        });

//        get specific user
        On.get("/user/{pk}").json((Req req) -> {

            Map user = ur.getUser(req.param("pk"));
            Resp resp = req.response();
            resp.json(user);
            resp.code(200);
            return resp;
        });

//        update user
        On.patch("/user/{pk}").json((Req req) -> {
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
            return resp;
        });

    }
}
