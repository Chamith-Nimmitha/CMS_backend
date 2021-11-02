package com.cms.wsocket;

import org.java_websocket.server.WebSocketServer;

public class TestServer {

    public static void main(String[] args) {

        NewHandler newHandler = new NewHandler();
        WebSocketServer webSocketServer = newHandler.getServerSocket();

            webSocketServer.start();

            String msg = "hello";
            int i = 1;
            while(true){
                webSocketServer.broadcast(msg+" "+i+"\n");
                i++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
        }
    }

}
