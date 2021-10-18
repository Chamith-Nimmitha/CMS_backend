package com.cms;
import com.cms.services.UserService;
import org.rapidoid.http.Req;
import org.rapidoid.setup.On;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rapidoid {

    public void runServer() {
        UserService ur = new UserService();

        On.get("/users").json(() -> {
            List results = null;
            results = ur.getUserList();
            List finalResults = results;
            return finalResults;
        });
        On.page("/about").html("Hello World");
        On.post("/user/new").json((Req req) -> {
            Map<String, Object> reqData = req.data();
            Map<Object, Object> data = new HashMap<>();
            data.put("name", reqData.get("name"));
            data.put("age", Integer.parseInt(reqData.get("age").toString()));
            ur.insertUser(data);
            return reqData;
        });
        On.get("/user/{pk}").html((String pk) -> {
            ur.deleteUser(pk);
            return "Deletion Successful";
        });

        On.get("/getuser/{pk}").json((String pk) -> {

            Map user = ur.getUser(pk);
            System.out.println(user);
            return user;
        });

    }
}
