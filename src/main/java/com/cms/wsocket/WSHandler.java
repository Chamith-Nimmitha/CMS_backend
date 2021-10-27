package com.cms.wsocket;

public class WSHandler {
    public static void main(String[] args) {

        System.out.println("Create a WSServer : ");
        WSServer wsServer = new WSServer();
        wsServer.start();

        while(WSServer.clientSockets.size() == 0){
            System.out.println("No client connected.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String msg = "hello";
        int i = 1;
        while(true){
            WSServer.sendMessage(msg+" "+i+"\n");
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
