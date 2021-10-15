package com.cms.config;

public class Configuration {

    private String dbTrpe;
    private String url;
    private String username;
    private String password;

    public String getDbTrpe() {
        return dbTrpe;
    }

    public void setDbTrpe(String dbTrpe) {
        this.dbTrpe = dbTrpe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
