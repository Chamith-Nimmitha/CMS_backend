package com.cms.wsocket;

import org.java_websocket.server.WebSocketServer;

public class NewHandler {

    private static WebSocketServer newServer = null;

    public NewHandler(){
        if(newServer == null){
            newServer = NewServer.create(5555, "127.0.0.1");
        }
    }

    public WebSocketServer getServerSocket(){
        return newServer;
    }
}
