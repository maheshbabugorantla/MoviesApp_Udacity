package com.example.mahes_000.moviesapp_udacity.DataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mahesh Babu Gorantla on 7/10/2016.
 */
public class ReviewItem {

    private String author;
    private String content;

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

    public String getAuthor()
    {
        return author;
    }

    public String getContent()
    {
        return content;
    }
}
