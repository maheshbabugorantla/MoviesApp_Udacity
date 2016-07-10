package com.example.mahes_000.moviesapp_udacity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mahes_000 on 7/10/2016.
 */
public class ReviewItem {

    public String author;
    public String content;

    // Constructor to convert the JSON Object into a Java Class Instance
    public ReviewItem(JSONObject jsonObject)
    {
       try {

           this.author =  jsonObject.getString("author");
           this.content = jsonObject.getString("content");
       }
       catch(JSONException e)
       {
           e.printStackTrace();
       }
    }
}
