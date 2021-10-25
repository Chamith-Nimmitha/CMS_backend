package com.cms.db;
import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB implements CommonDB {

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
            where.put("_id", new ObjectId(pk));
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
