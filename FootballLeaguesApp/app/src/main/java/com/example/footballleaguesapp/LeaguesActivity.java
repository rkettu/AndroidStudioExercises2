package com.example.footballleaguesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaguesActivity extends AppCompatActivity {

    private RequestQueue queue;
    private List<String> leagues;
    private ListView leaguesListView;
    private ArrayAdapter<String> aa;
    private String areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);

        Intent intent = getIntent();
        areaId = intent.getStringExtra("MYKEY");

        queue = Volley.newRequestQueue(this);

        leagues = new ArrayList<>();

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leagues);

        leaguesListView = (ListView) findViewById(R.id.leaguesList);
        leaguesListView.setAdapter(aa);

        String url = "https://api.football-data.org/v2/competitions?areas=" + areaId;
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Got a response
                List<String> resultsList = MyParser.parseLeagueNames(response);
                leagues.clear();
                leagues.addAll(resultsList);
                if(leagues.size() == 0) leagues.add("No Leagues in this country...");
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
