package com.cms.wsocket;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.util.Collection;


public class NewServer  extends WebSocketServer{

    private NewServer(String host, int port) {
        super(new InetSocketAddress(host, port));
    }
    public static NewServer create(int port) {
        return create(port, "0.0.0.0");
    }
    public static NewServer create(int port, String host) {
        return new NewServer(host, port);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("connected");
    }

    @Override
    public Collection<WebSocket> getConnections() {
        return super.getConnections();
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("closed");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {

        ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
        builder.put("Access-Control-Allow-Origin", "*");
        return builder;
    }

    private String getUri(String uri) {
        int index = uri.indexOf("?");
        if (index == -1)
            return uri.substring(1);
        return uri.substring(1, index);
    }

}
