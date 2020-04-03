package com.example.stockmonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "EXCEPTION ALERT";
    private ListView stocksListView;
    private List<String> stocksList;
    private ArrayAdapter<String> arrayAdapter;
    private EditText stockNameField;
    private EditText stockIdField;
    private HashMap<String,String> stockIdNameMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockIdNameMap = new HashMap<String, String>();
        stockIdNameMap.put("AAPL", "Apple");
        stockIdNameMap.put("GOOGL", "Alphabet (Google)");
        stockIdNameMap.put("FB", "Facebook");
        stockIdNameMap.put("NOK", "Nokia");

        stocksList = new ArrayList<>();

        stocksListView = (ListView) findViewById(R.id.stocksList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocksList);
        stocksListView.setAdapter(arrayAdapter);

        new MyAsyncInnerClass().execute("AAPL", "GOOGL", "FB", "NOK");

        stockNameField = (EditText) findViewById(R.id.stockNameField);
        stockIdField = (EditText) findViewById(R.id.stockIdField);

        final Button button = (Button) findViewById(R.id.stockAddButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = stockNameField.getText().toString();
                String id = stockIdField.getText().toString();
                stockIdNameMap.put(id,name);

                new MyAsyncInnerClass().execute(id);
            }
        });
    }

    private class MyAsyncInnerClass extends AsyncTask<String, Void, HashMap<String,String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(String... stockNames) {
            Log.d("EXECUTING", "Executing doInbackground, input array length: " + stockNames.length);
            String base = "https://financialmodelingprep.com/api/company/price/";
            HttpURLConnection conn = null;
            HashMap<String,String> resultHm = new HashMap<String,String>();

            for(int i = 0; i < stockNames.length; i++)
            {
                String myUrl = base + stockNames[i];

                try {
                    URL url = new URL(myUrl);
                    conn = (HttpURLConnection) url.openConnection();
                } catch(Exception e) {
                    Log.d(TAG, ("Something went wrong: " + e.toString()));
                    e.printStackTrace();
                }

                String resultJsonString = null;

                try {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                    {
                        total.append(line).append('\n');
                    }
                    resultJsonString = total.toString();
                } catch(Exception e) {
                    Log.d(TAG, ("Something went wrong: " + e.toString()));
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }
                String parseResult = ParseJson(resultJsonString,stockNames[i]);

                if(!parseResult.equals(""))
                    resultHm.put(stockNames[i], parseResult);
            }

            return resultHm;
        }

        @Override
        protected void onPostExecute(HashMap<String,String> resultHm) {
            try {
                for(Map.Entry<String,String> entry : resultHm.entrySet()) {
                    stocksList.add(stockIdNameMap.get(entry.getKey()) + ": " + entry.getValue() + " USD");
                    arrayAdapter.notifyDataSetChanged();
                }
            } catch(Exception e) {
                Log.d(TAG, ("Something went wrong: " + e.toString()));
                e.printStackTrace();
            }
        }

        private String ParseJson(String htmlString, String stockId)
        {

            String str = null;
            JSONObject jObj = null;

            // Removing html tags...
            String jsonString = android.text.Html.fromHtml(htmlString).toString();

            try {
                jObj = new JSONObject(jsonString);
            } catch(Exception e) {
                Log.d(TAG, ("Something went wrong: " + e.toString()));
                e.printStackTrace();
            }

            String price = "";

            try {
                price = ((JSONObject)jObj.get(stockId)).get("price").toString();
            } catch(Exception e) {
                Log.d(TAG, ("Something went wrong: " + e.toString()));
                e.printStackTrace();
            }

            return price;
        }
    }
}
