package com.example.footballleaguesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String ERR = "ERROR_ERROR_ERROR";
    private ListView leaguestListView;
    private ArrayAdapter<String> aa;
    private List<String> leagues;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        leagues = new ArrayList<>();

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leagues);

        leaguestListView = (ListView) findViewById(R.id.leaguesList);
        leaguestListView.setAdapter(aa);

        String url = "https://api.football-data.org/v2/competitions?areas=2072";
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Got a response
                List<String> resultsList = parseLeagueNames(response);
                leagues.clear();
                leagues.addAll(resultsList);
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

    private List<String> parseLeagueNames(JSONObject jObj)
    {
        List<String> list = new ArrayList<>();

        try
        {
            JSONArray competitions = jObj.getJSONArray("competitions");
            for(int i = 0; i < competitions.length(); i++)
            {
                String name = ((JSONObject)competitions.get(i)).getString("name");
                list.add(name);
            }
        } catch(Exception e) {
            Log.d(ERR, "Exception: " + e.toString());
            e.printStackTrace();
        }

        return list;
    }
}
