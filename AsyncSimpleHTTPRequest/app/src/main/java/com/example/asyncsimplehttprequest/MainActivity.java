package com.example.asyncsimplehttprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText addressField;
    private TextView textBox;
    private final String TAG = "EXCEPTION ALERT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textBox = (TextView) findViewById(R.id.textBox);
        addressField = (EditText) findViewById(R.id.addressField);

        final Button btn = (Button) findViewById(R.id.fetchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressField.getText().toString();
                FetchData(address);
            }
        });


    }

    private void FetchData(String address)
    {
        HttpURLConnection conn = null;


        try {
            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
        } catch(Exception e) {
            Log.d(TAG, ("Something went wrong: " + e.toString()));
            e.printStackTrace();
        }

        if(conn == null) return;

        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                total.append(line).append('\n');
            }
            textBox.setText(total.toString());
        } catch(Exception e) {
            Log.d(TAG, ("Something went wrong: " + e.toString()));
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }


    }


}
