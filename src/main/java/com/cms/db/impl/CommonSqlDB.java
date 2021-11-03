package com.cms.db.impl;

import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.db.CommonDB;

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
    public Map<String, Map<String, Object>> getTableMetaData(String table) throws SQLException {
        String query = "SELECT * from " + table + " LIMIT 1;";
        Statement st = this.con.createStatement();
        ResultSet rs = st.executeQuery(query);
        ResultSetMetaData metaData = rs.getMetaData();

        Map<String, Map<String, Object>> columns = new HashMap<>();

        String cName;
        String cType;
        for( int i=1; i <= metaData.getColumnCount(); i++){
            cName =metaData.getColumnName(i);
            cType = metaData.getColumnTypeName(i);

            Map<String, Object> col = new HashMap<>();
            col.put("name", cName);
            col.put("dataType", cType);
            columns.put(cName, col);
        }
        return columns;
    }

    @Override
    public boolean createTableSchema(Map<String, Object> metaData) throws Exception {
        String tableName = (String) metaData.get("tableName");
        List<Map<String, Object>> columns = (List<Map<String, Object>>) metaData.get("columns");

        String query = "CREATE TABLE " + tableName + "(";
        boolean isFirstColumn = true;

        for (Map<String, Object> column: columns){
            String cName = column.get("name").toString();
            String cType = column.get("dataType").toString();
            boolean isPrimaryKey = Boolean.parseBoolean(column.getOrDefault("isPrimary", "0").toString());
            int length = Integer.parseInt(column.get("length").toString());

            if( isFirstColumn){
                isFirstColumn = false;
            }else{
                query += ", ";
            }

            query += cName + " ";

            switch (cType){
                case "INT":
                    query += "INT(" + length + ") ";
                    break;
                case "VARCHAR":
                    query += "VARCHAR(" + length + ") ";
                    break;
                default:
                    throw new Exception("Column dataType not match. " + cType +" is not a valid type.");
            }

            if(isPrimaryKey){
                query += "PRIMARY KEY ";
            }
        }
        query += ")";
        Statement st = this.con.createStatement();
        int result = st.executeUpdate(query);
        if(result == 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List select(String table) throws SQLException {

        List li = new LinkedList();

        String query = "SELECT * from " + table;
        Statement st = this.con.createStatement();
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
    public void insert(String table, Map<String, Object> data) throws SQLException {
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
