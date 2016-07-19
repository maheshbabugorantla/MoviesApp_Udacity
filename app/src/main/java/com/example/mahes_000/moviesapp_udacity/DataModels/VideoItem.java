package com.example.mahes_000.moviesapp_udacity.DataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mahes_000 on 7/11/2016.
 */
public class VideoItem {

    private String Key_value;

    public VideoItem(JSONObject jsonObject)
    {
        try
        {
            this.Key_value = jsonObject.getString("key");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public String getURL()
    {
        return Key_value;
    }

}
