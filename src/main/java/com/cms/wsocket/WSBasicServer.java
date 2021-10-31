package com.cms.wsocket;

import com.sun.corba.se.impl.ior.ByteBuffer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WSBasicServer extends Thread{

    public static Queue<Socket> clientSockets = new ConcurrentLinkedQueue();
    static ServerSocket serverSocket;

    WSBasicServer(){
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
                onOpen(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onOpen(Socket s) throws IOException {

        InputStream inputStream = s.getInputStream();
        OutputStream outputStream = s.getOutputStream();
        Scanner sc = new Scanner(inputStream, "UTF-8");

        Scanner scanner = sc.useDelimiter("\\r\\n\\r\\n");
        String data = scanner.next();
        Matcher get = Pattern.compile("^GET").matcher(data);

        if (get.find()) {
            Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
            match.find();

            String response=null;
            try {
                response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Access-Control-Allow-Origin: *\r\n"
                        + "Access-Control-Allow-Headers: *\r\n"
                        + "Access-Control-Allow-Methods: *\r\n"
                        + "Sec-WebSocket-Accept: "
                        + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                        + "\r\n\r\n");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] resBytes = response.getBytes("UTF-8");
            outputStream.write(resBytes, 0, resBytes.length);
        }

        boolean success = clientSockets.add(s);
        ListenToClientThread listenToClientThread = new ListenToClientThread(s);
        listenToClientThread.start();

        if(success){
            System.out.format("\nClient %s:%s connected.", s.getLocalAddress(),s.getPort());
        }else{
            System.out.println("Fail to add new client socket.");
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

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        ByteBuffer byteBuffer = new ByteBuffer();
        int available = 0, reminder=0;

        int c;
        while(true){
            try {
                if(reminder == 0){
                    available = inputStream.available();
                    if(available == 0){
                        continue;
                    }
                    reminder = available;
                    System.out.println("Available : "+ available);
                }
                if ((c=inputStream.read()) == -1) break;
                    byteBuffer.append((byte)c);
                    reminder--;
                if(reminder == 0){
                    decodeMsg(Arrays.copyOfRange(byteBuffer.toArray(),0, available));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

//    decode messages
    public void decodeMsg(byte[] data){

        if(data.length < 6){
            return;
        }
        System.out.println(Arrays.toString(data));
        int msgLength = data.length;
        ByteBuffer decoded = new ByteBuffer();
        byte[] key = new byte[4];
        int count = 0;
        for( byte b: data){
            if(count==0 || count==1){
                count++;
                continue;
            }
            if (count==2 || count==3|| count==4 || count==5){
                key[count-2] = b;
                count++;
            }
            else{
                break;
            }
        }
        byte[] encoded = Arrays.copyOfRange(data,6, msgLength);
        for (int i = 0; i < encoded.length; i++) {
            decoded.append((byte) (encoded[i] ^ key[i & 0x3]));
        }
        System.out.println(new String(Arrays.copyOfRange(decoded.toArray(), 0, msgLength - 6)));
    }
}