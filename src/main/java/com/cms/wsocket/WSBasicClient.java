package com.cms.wsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class WSBasicClient {

    private static Socket clientSocket;

    public static void main(String[] args) throws IOException {
        try {
            clientSocket = new Socket("127.0.0.1", 5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        System.out.println("called");
        int c;
        while( (c=bufferedReader.read()) !=-1){
            System.out.print((char)c);
        }
    }
}
