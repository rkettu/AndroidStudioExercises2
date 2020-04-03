package com.example.websocketchat;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

interface EchoClientInterface {
    void onMessage(String message);
    void onStatusChange(String newStatus);
}

public class EchoClient extends WebSocketClient{
    EchoClientInterface observer;

    public EchoClient(URI serverUri, EchoClientInterface observer) {
        super(serverUri);
        this.observer = observer;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("SOCKET", "onOpen called");
        observer.onStatusChange("Connection open");
    }

    @Override
    public void onMessage(String message) {
        Log.d("SOCKET", "onMessage called");
        observer.onMessage(message);
        observer.onStatusChange("Received message");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("SOCKET", "onClose called");
        observer.onStatusChange("Socket closed");
    }

    @Override
    public void onError(Exception e) {
        Log.d("SOCKET", "onError called");
        observer.onStatusChange("Error in socket:" + e.toString());
    }
}
