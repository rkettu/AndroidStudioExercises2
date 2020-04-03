package com.example.footballleaguesapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyParser {
    private static final String ERR = "ERROR_ERROR_ERROR";

    public static List<Area> parseAreas(JSONObject jObj)
    {
        List<Area> list = new ArrayList<>();

        try
        {
            JSONArray areas = jObj.getJSONArray("areas");
            for(int i = 0; i < areas.length(); i++)
            {
                String areaId = ((JSONObject)areas.get(i)).getString("id");
                String areaName = ((JSONObject)areas.get(i)).getString("name");
                list.add(new Area(areaId,areaName));
            }
        } catch(Exception e) {
            Log.d(ERR, "Exception: " + e.toString());
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> parseLeagueNames(JSONObject jObj)
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
