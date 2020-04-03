package com.example.simplevolleyrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private EditText addressField;
    private TextView textBox;
    private final String TAG = "EXCEPTION ALERT";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        textBox = (TextView) findViewById(R.id.textBox);
        addressField = (EditText) findViewById(R.id.addressField);

        final Button btn = (Button) findViewById(R.id.fetchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressField.getText().toString();
                VolleyRequest(address);
            }
        });
    }

    private void VolleyRequest(String url)
    {
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    textBox.setText(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textBox.setText("ERROR: " + error.toString());
                }
        });

        queue.add(req);

    }
}
