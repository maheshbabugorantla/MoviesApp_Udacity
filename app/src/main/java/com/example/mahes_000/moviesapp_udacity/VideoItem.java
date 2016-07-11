package com.example.mahes_000.moviesapp_udacity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mahes_000 on 7/11/2016.
 */
public class VideoItem {

    public String Key_value;

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

}
