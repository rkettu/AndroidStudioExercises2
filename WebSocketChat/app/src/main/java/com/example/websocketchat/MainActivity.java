package com.example.websocketchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements EchoClientInterface {

    private EchoClient echoClient = null;

    private EditText messageField;
    private TextView messageBox;
    private URI uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            uri = new URI("ws://obscure-waters-98157.herokuapp.com");
        } catch(Exception e) {
            e.printStackTrace();
        }



        messageBox = (TextView) findViewById(R.id.messageBox);
        messageField = (EditText) findViewById(R.id.messageField);

        if(uri != null)
            openConnection();
    }

    public void sendMessage(View v)
    {
        if(echoClient != null && echoClient.isOpen())
        {
            String myMessage = messageField.getText().toString();
            echoClient.send(myMessage);
        }
        else
        {
            messageBox.append("Not connected - attempting to reconnect");
            openConnection();
        }
    }

    private void openConnection() {
        try {
            echoClient = new EchoClient(uri, this);
            echoClient.connect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageBox.append(message + "\n");
            }
        });
    }

    @Override
    public void onStatusChange(final String newStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageBox.append(newStatus + "\n");
            }
        });
    }
}
