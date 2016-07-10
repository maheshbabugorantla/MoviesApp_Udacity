package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by Mahesh Babu Gorantla on 7/9/2016.
 *
 * The Fragment Below is used to display the Reviews of the MoviesApp_Udacity.
 *
 */
public class Reviews_Fragment extends Fragment {

    String Movies_Data = null;
    String Video_Choice = null;

    private static final String LOG_TAG = Reviews_Fragment.class.getSimpleName();

    private boolean isNetworkAvailable() {
        /*
            Here I have to use getActivity() for getSystemService() Function is because the getSystemService() function will require context.
            Hence, using getActivity() will give us the context of the Activity in the Non-Activity Class and also as getActivity() extends Context.
            Calling getActivity() will return the Context of the App.
        */

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View Reviews_View = inflater.inflate(R.layout.fragment_reviews, container, false);

        Intent intent = getActivity().getIntent();
        boolean NetworkStatus = isNetworkAvailable();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT) && NetworkStatus)
        {
            String intent_text = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] intent_values = intent_text.split("=");

            String ID = intent_values[7];
            Video_Choice = intent_values[6];

            // Fetching the Data for the Movie.
            boolean reviews_present = getMovieData(ID, Video_Choice);

            // Setting the Adapter for the ListView
            ArrayList<ReviewItem> reviews = new ArrayList<>();
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), reviews);

            if(reviews_present) {
                reviews.clear();
                reviews.addAll(getJSONData(Movies_Data));
            }
            else {
                reviews.clear();

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", "No Reviews Available yet for this TV Show, Please check back again");
                    jsonObject.put("author", "Anonymous");
                    reviews.add(new ReviewItem(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ListView reviews_list = (ListView) Reviews_View.findViewById(R.id.reviews_list);
            reviews_list.setAdapter(reviewsAdapter);
        }

/*
        else
        {
            Reviews = "No Internet Available!\n\nConnect to the Internet and Re-Open the App";
            ((TextView) Reviews_View.findViewById(R.id.reviews)).setText("Reviews:\n" + Reviews);
        }
*/
        return Reviews_View;
    }

    private boolean getMovieData(String movie_choice, String video_choice) {

        if (video_choice.equals("movie")) {

            DownloadTask downloadTask = new DownloadTask();

            try {
                downloadTask.execute(movie_choice, video_choice).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
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


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return (getData(urls[0], urls[1]));
        }

        public String getData(String url, String Video_Type) {
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

            return (Movies_Data);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
        }
    }


}
