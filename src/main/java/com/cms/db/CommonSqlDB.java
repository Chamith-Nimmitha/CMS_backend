package com.cms.db;

import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class CommonSqlDB implements CommonDB {

    private String url;
    private String username;
    private String password;
    private Connection con;

    public CommonSqlDB() {
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

        this.url = configuration.getUrl();
        this.username = configuration.getUsername();
        this.password = configuration.getPassword();
        try {
            this.con = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Db connection failed. Plz check DB server is working properly.");
            System.exit(0);
        }
    }

    @Override
    public void createConnection() throws SQLException {
        this.con  = DriverManager.getConnection(this.url, this.username, this.password);
        System.out.println("Connection established");
    }

    @Override
    public CommonDB getDB() {
        return (CommonDB)this;
    }

    @Override
    public List select(String table) throws SQLException {
        String query = "SELECT * from " + table;
        Statement st = null;
        List li = new LinkedList();

        st = this.con.createStatement();
        ResultSet rs = st.executeQuery(query);
        ResultSetMetaData metaData = rs.getMetaData();
        String cName;
        String cType;

        List data = new ArrayList();
        while (rs.next()) {
            Map<String, Object> dataRow = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                cName = metaData.getColumnName(i);
                cType = metaData.getColumnTypeName(i);

                if (cType == "VARCHAR") {
                    dataRow.put(cName, rs.getString(cName));
                } else if (cType == "INT") {
                    dataRow.put(cName, rs.getInt(cName));
                }
            }
            data.add(dataRow);
        }
        return data;
    }

    @Override
    public List select(String table, Map<String, String> where) throws SQLException {
        String query = "SELECT * from " + table;
        boolean isFirstWhere = true;
        Statement st = null;
        List li = new LinkedList();

        for(String k: where.keySet()){
            if(isFirstWhere){
                query += " WHERE " + k +"='"+ where.get(k)+"'";
                isFirstWhere = false;
            }else{
                query += " AND " + k + "='"+where.get(k)+"'";
            }
        }
        st = this.con.createStatement();
        ResultSet rs = null;
        System.out.println(query);
        rs = st.executeQuery(query);
        ResultSetMetaData metaData = rs.getMetaData();
        String cName;
        String cType;

        List data = new ArrayList();
        while (rs.next()) {
            Map<String, Object> dataRow = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                cName = metaData.getColumnName(i);
                cType = metaData.getColumnTypeName(i);

                if (cType == "VARCHAR") {
                    dataRow.put(cName, rs.getString(cName));
                } else if (cType == "INT") {
                    dataRow.put(cName, rs.getInt(cName));
                }
            }
            data.add(dataRow);
        }
        return data;
    }

    @Override
    public void insert(String table, Map<Object, Object> data) throws SQLException {
        String query = "INSERT INTO "+ table + " ";
        final String[] keys = {""};
        final String[] values = {""};

        for (Object k : data.keySet()) {
            if (keys[0].length() != 0) {
                keys[0] += ", ";
                values[0] += ", ";
            }
            keys[0] += " " + k;
            values[0] += "'" +data.get(k) + "'";
        }

        query += "(" + keys[0] + ")";
        query += " VALUES (" + values[0] + ");";

        Statement st = this.con.createStatement();
        st.executeUpdate(query);
        return;
    }

    @Override
    public int delete(String table, String pk) throws SQLException {

        String query = "DELETE FROM "+ table + " WHERE id='"+pk+"';";
        System.out.println(query);
        Statement st = this.con.createStatement();
        int count = st.executeUpdate(query);

        if(count == 1){
            return 1;
        }else{
            return 0;
        }

    }

    @Override
    public boolean update(String table, Map<String, Object> data, Map<String, String> where) throws SQLException {
        String query = "UPDATE "+ table + " SET ";
        boolean isFirstField = true;
        boolean isFirstWhere = true;

        for (Object k : data.keySet()) {
            if(isFirstField){
                query += k + " = '" + data.get(k) + "' ";
                isFirstField = false;
            }else{
                query += ", "+  k + " = '" + data.get(k) + "' ";
            }

        }

        for(String k: where.keySet()){
            if(isFirstWhere){
                query += " WHERE " + k +"='"+ where.get(k)+"';";
                isFirstWhere = false;
            }else{
                query += " AND " + k + "='"+ where.get(k)+"';";
            }
        }
        System.out.println(query);
        Statement st = this.con.createStatement();
        int result = st.executeUpdate(query);

        if(result == 1){
            return true;
        }else{
            return false;
        }
    }
}
