package com.cms.db;
import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB implements CommonDB {

    MongoDatabase DB;
    public MongoDB(){
        Configuration configuration = null;
        //  load db configuration
        try {
            configuration = ConfigurationManager
                    .getInstance()
                    .loadConfiguration("src/main/resources/applicationConfig.json");
        } catch (IOException e) {
            System.out.println("Configuration manager failed.");
            System.exit(0);
        }
        String url = configuration.getUrl();
        try {
            String dbname = "MyDatabase";
    //        MongoClient mongoClient = MongoClients.create(uri);
    //        this.DB = mongoClient.getDatabase(dbname);

                MongoClient mongoClient = MongoClients.create(url);

    //        MongoIterable<String> dbNames = mongoClient.listDatabaseNames();
    //        for(String name: dbNames){
    //            System.out.println(name);
    //       }
                this.DB = mongoClient.getDatabase(dbname);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Db connection failed. Plz check DB server is working properly.");
            System.exit(0);
        }
    }

    @Override
    public void createConnection(){

    }

    @Override
    public CommonDB getDB() {
        return (CommonDB) this;
    }

    @Override
    public List select(String table) throws Exception {
        MongoCollection<Document> users = this.DB.getCollection(table);
        FindIterable<Document> doc = users.find();
        ArrayList arrayList = new ArrayList();
        doc.forEach( x -> {
            Map<String, Object> newData = new HashMap<>();
            for(String k: x.keySet()) {
                newData.put(k, x.get(k).toString());
            }
            arrayList.add(newData);
        });
        return arrayList;
    }

    @Override
    public List select(String table, Map<String, String> where) {
        return null;
    }

    @Override
    public void insert(String table, Map<Object, Object> data) throws Exception {
        MongoCollection<Document> users = this.DB.getCollection(table);
        Document newDoc = new Document();
        data.keySet().forEach(x -> {
            newDoc.append(x.toString(), data.get(x));
        });
        users.insertOne(newDoc);
        return;
    }

    @Override
    public int delete(String table, String pk) {

        try {
            MongoCollection<Document> users = this.DB.getCollection(table);
            users.deleteOne(eq("id", pk));
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public boolean update(String table, Map<String, Object> data, Map<String, String> where) {
        return true;
    }
}
