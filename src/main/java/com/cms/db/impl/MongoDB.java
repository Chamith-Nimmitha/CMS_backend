package com.cms.db.impl;
import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.db.CommonNosqlDB;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.util.*;

public class MongoDB implements CommonNosqlDB {

    MongoDatabase DB;
    public MongoDB(){
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

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

    private String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder rndString = new StringBuilder();
        Random rnd = new Random();
        while (rndString.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            rndString.append(SALTCHARS.charAt(index));
        }
        String saltStr = rndString.toString();
        return saltStr;

    }

    @Override
    public Set<String> getTableNames() throws SQLException {
        return null;
    }

    @Override
    public Map<String, Map<String, Object>> getTableMetaData(String table) {
        return null;
    }

    @Override
    public boolean createTableSchema(Map<String, Object> metaData) throws Exception {
        return false;
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
        MongoCollection<Document> users = DB.getCollection("user");

        BasicDBObject query = new BasicDBObject();
        for(String k: where.keySet()){
            query.put(k, where.get(k));
        }
        FindIterable<Document> documents = users.find(query);
        Iterator i = documents.iterator();
        ArrayList data = new ArrayList();
        while(i.hasNext()){
            Map<String,String> row = new HashMap();
            Document next = (Document) i.next();
            for( String k: next.keySet()){
                row.put(k, next.get(k).toString());
            }
            data.add(row);
        }
        return data;
    }

    @Override
    public void insert(String table, Map<String, Object> data) throws Exception {
        MongoCollection<Document> users = this.DB.getCollection(table);
        Document newDoc = new Document();
        newDoc.append("id", getRandomString());
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
            BasicDBObject where = new BasicDBObject();
            where.put("id", new ObjectId(pk));
            users.deleteOne(where);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public boolean update(String table, Map<String, Object> data, Map<String, String> where) {
        MongoCollection<Document> users = DB.getCollection("user");

        BasicDBObject query = new BasicDBObject();

        for( String k: where.keySet()){
            query.put(k, new ObjectId(where.get(k)));
        }

        BasicDBObject newDocument = new BasicDBObject();

        for(String k: data.keySet()){
            newDocument.put(k, data.get(k));
        }

        BasicDBObject updatedObject = new BasicDBObject();
        updatedObject.put("$set", newDocument);

        UpdateResult updateResult = users.updateMany(query, updatedObject);
        System.out.println(updateResult);
        return true;
    }
}
