package com.example.asyncsimplehttprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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

        textBox = (TextView) findViewById(R.id.textBox);
        addressField = (EditText) findViewById(R.id.addressField);

        final Button btn = (Button) findViewById(R.id.fetchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressField.getText().toString();
                new MyAsyncInnerClass().execute(address);
            }
        });
    }

    private class MyAsyncInnerClass extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... myUrl) {

            HttpURLConnection conn = null;
            String resultString = null;

            try {
                URL url = new URL(myUrl[0]);
                conn = (HttpURLConnection) url.openConnection();
            } catch(Exception e) {
                Log.d(TAG, ("Something went wrong: " + e.toString()));
                e.printStackTrace();
            }

            if(conn == null) return "Connection error";

            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    total.append(line).append('\n');
                }
                resultString = total.toString();
            } catch(Exception e) {
                Log.d(TAG, ("Something went wrong: " + e.toString()));
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }

            return resultString;
        }

        protected void onPostExecute(String result) {
            textBox.setText(result);
        }

    }

}
