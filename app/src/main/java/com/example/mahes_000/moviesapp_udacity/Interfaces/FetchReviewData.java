package com.example.mahes_000.moviesapp_udacity.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mahes_000.moviesapp_udacity.BuildConfig;
import com.example.mahes_000.moviesapp_udacity.R;
import com.example.mahes_000.moviesapp_udacity.ReviewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *  Created by Mahesh Babu Gorantla on 7/16/2016.
 *
 *  The Code below is used to Fetch Reviews for Movie/TV Show
 */
public class FetchReviewData extends AsyncTask<String, Void, ArrayList<ReviewItem>> {


    String Movies_Data = null;

    String Video_Choice = null;


    private static final String LOG_TAG = FetchReviewData.class.getSimpleName();


    public FetchReviewData(Context context) {

        // Getting the User Preferences.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Video_Choice = sharedPreferences.getString(context.getString(R.string.pref_video_choice_key), context.getString(R.string.pref_video_choice_default));

    }

    private ArrayList<ReviewItem> getJSONData(String json_data) {

        ArrayList<ReviewItem> reviewItems = new ArrayList<>();

        try {
            // Parsing the JSON Data in the form of a String
            JSONObject reader = new JSONObject(json_data);

            int total_review_count = Integer.parseInt(reader.getString("total_results"));

            Log.d(LOG_TAG, "Inside JSON Function, " + Integer.toString(total_review_count));

            if(total_review_count > 0)
            {
                JSONArray reviews = reader.getJSONArray("results");

                int review_count = reviews.length();

                for (int index = 0; index < review_count; index++)
                {
                    reviewItems.add(new ReviewItem(reviews.getJSONObject(index)));
                }
            }
            else
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content","No Reviews Available yet for this Movie");
                jsonObject.put("author", "Anonymous");
                reviewItems.add(new ReviewItem(jsonObject));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MoviesApp JSONException", " JSONException Occurred in Review Fragment getJSONData Function");
        }

        return reviewItems;
    }

    @Override
    protected ArrayList<ReviewItem> doInBackground(String... urls) {
        return (getData(urls[0], urls[1]));
    }

    public ArrayList<ReviewItem> getData(String url, String Video_Type) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";
        final String API_Key = BuildConfig.THE_MOVIE_DB_API_KEY;
        final String ID = "api_key";

        Uri BuiltUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(Video_Type).appendPath(url).appendPath("reviews").appendQueryParameter(ID, API_Key).build();

        String BuiltURL = BuiltUri.toString();

        Log.d("Movie URL", BuiltURL);

        try {
            URL web_url = new URL(BuiltURL);

            // Creates a request to TheMovieDB Website and opens the Connection
            httpURLConnection = (HttpURLConnection) web_url.openConnection();
            httpURLConnection.setRequestMethod("GET"); // Letting the Connection know that the data will be fetched
            httpURLConnection.connect(); // Connecting to the Web Link

            // Read the Input Stream into the String
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            // This happens when there is no data being read from the Opened Connection to Web URL.
            if (inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Now reading the raw JSON Data into BufferReader
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

            // This might happen when read from URL becomes unsuccessful or may be the connection might drop in between
            if (buffer.length() == 0) {
                return null;
            }

            Movies_Data = buffer.toString();

            return(getJSONData(Movies_Data));
        }

        // This Exception will occur whenever there is MALFORMED URL
        catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("MoviesApp MalformedURL:", " Malformed URL Exception Occurred");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MoviesApp IOException: ", "IO Exception Occurred");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MoviesApp Exception: ", " Unknown Exception Occurred");
        }

        // Here Closing all the Open Network Connections and the BufferedReaders.
        finally {
            // Checking to see if the connection to web URL is still Open. If yes, then close it.
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("MoviesApp IOException: ", " IO Exception occurred while closing the reader");
                }
            }
        }

        return (null);
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewItem> reviews) {
        super.onPostExecute(reviews);
    }
}

