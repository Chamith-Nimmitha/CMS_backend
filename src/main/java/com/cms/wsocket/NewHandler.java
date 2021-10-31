package com.cms.wsocket;

public class NewHandler {

    public static void main(String[] args) {
        NewServer newServer = NewServer.create(5555, "127.0.0.1");
        newServer.start();

        String msg = "hello";
        int i = 1;
        while(true){
           newServer.broadcast(msg+" "+i+"\n");
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
