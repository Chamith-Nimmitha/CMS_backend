package com.cms.wsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WSServer extends Thread{

    public static Queue<Socket> clientSockets = new ConcurrentLinkedQueue();
    static ServerSocket serverSocket;

    WSServer(){
        if(serverSocket == null){
            try {
                serverSocket = new ServerSocket(5555);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void run(){
        runServer();
    }

    public void runServer(){
        while(true){
            try {
                Socket s = serverSocket.accept();
                boolean success = clientSockets.add(s);
                ListenToClientThread listenToClientThread = new ListenToClientThread(s);
                listenToClientThread.start();
                if(success){
                    System.out.format("\nClient %s:%s connected.", s.getLocalAddress(),s.getPort());
                }else{
                    System.out.println("Fail to add new client socket.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendMessage(String message){
        SendMessageThread sendMessageThread = new SendMessageThread(clientSockets, message);
        sendMessageThread.start();
    }

}

class SendMessageThread extends Thread{

    private final String message;
    private Queue<Socket> clientSockets;

    SendMessageThread(Queue q, String message){
        this.clientSockets = q;
        this.message = message;
    }


    public void run(){
        for(Socket s: this.clientSockets) {
            PrintWriter p = null;
            try {
                System.out.println("S -> Ready to sent msg : "+message);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(s.getOutputStream());
                p = new PrintWriter(outputStreamWriter);
//                p.print(message);
                outputStreamWriter.write(message);
                outputStreamWriter.flush();

            } catch (IOException e) {
                System.out.format("Client %s:%s exit.", s.getLocalAddress(),s.getPort());
                this.clientSockets.remove(s);
            }
        }
    }
}


class ListenToClientThread extends Thread{

    private Socket clientSocket;

    ListenToClientThread(Socket s ){
        this.clientSocket = s;
    }

    public void run() {

        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            Thread.currentThread().stop();
        }

        int c;
        while(true){
            try {
                if ((c=inputStream.read()) == -1) break;
                System.out.println((char)c);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}