package com.example.footballleaguesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView countriesListView;
    private ArrayAdapter<String> aa;
    private List<String> areaNamesList;
    private RequestQueue queue;
    private HashMap<String,String> nameIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameIdMap = new HashMap<String,String>();

        queue = Volley.newRequestQueue(this);

        areaNamesList = new ArrayList<>();

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areaNamesList);

        countriesListView = (ListView) findViewById(R.id.countriesList);
        countriesListView.setAdapter(aa);

        countriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = areaNamesList.get(position);
                String countryId = nameIdMap.get(name);
                Log.d("ITEMCLICKEDALERT", "Name: " + name + " ID: " + countryId);
                Intent intent = new Intent(MainActivity.this, LeaguesActivity.class);
                intent.putExtra("MYKEY", countryId);
                startActivity(intent);
            }
        });

        String url = "https://api.football-data.org/v2/areas";
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Got a response
                List<Area> resultsList = MyParser.parseAreas(response);
                List<String> areaNames = new ArrayList<>();
                for(Area a : resultsList)
                {
                    nameIdMap.put(a.getName(), a.getId());
                    areaNames.add(a.getName());
                }
                areaNamesList.clear();
                areaNamesList.addAll(areaNames);
                aa.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error
            }
        });

        queue.add(jsonReq);
    }


}
