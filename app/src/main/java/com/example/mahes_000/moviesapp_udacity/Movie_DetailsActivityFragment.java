package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class Movie_DetailsActivityFragment extends Fragment
{
    String Movies_Data = null;
    String[] final_values = null;
    String Video_Choice = null;

    public Movie_DetailsActivityFragment()
    {
        setHasOptionsMenu(true);
    }

    private boolean isNetworkAvailable()
    {
        /*
            Here I have to use getActivity() for getSystemService() Function is because the getSystemService() function will require context.
            Hence, using getActivity() will give us the context of the Activity in the Non-Activity Class and also as getActivity() extends Context.
            Calling getActivity() will return the Context of the App.
        */

        ConnectivityManager connectivityManager =  (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Creating a rootView
        View rootView = inflater.inflate(R.layout.fragment_movie__details, container, false);

        Intent intent = getActivity().getIntent();

        boolean networkStatus = isNetworkAvailable();

        if((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT) && networkStatus)
        {
            String text_detail = intent.getStringExtra(Intent.EXTRA_TEXT);

            String[] str_values = text_detail.split("=");

            String Image_Base_URL = "http://image.tmdb.org/t/p/w185/";
            String Image_URL = Image_Base_URL + str_values[0];

            // Setting the Image for the Poster
            Picasso.with(getActivity()).load(Image_URL).fit().into((ImageView) rootView.findViewById(R.id.details_poster));

            ((TextView) rootView.findViewById(R.id.movie_title)).setText(str_values[2]); // Movie Title
            ((TextView) rootView.findViewById(R.id.story_detail)).setText(str_values[1]); // Movie Story
            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText(str_values[3] + "/10"); // User Rating Value

            ((TextView) rootView.findViewById(R.id.release_date_value)).setText(str_values[5].split("-")[0]);  // Release Date (Movie) or First Aired Date (TV)


            /*
            *   6. Video_Choice
            *   7. Id
            * */

            Video_Choice = str_values[6];
            String Movie_ID = str_values[7];

            // Fetching the Data for the Movie.
            getMovieData(Movie_ID, Video_Choice);

            String Run_Time = final_values[0];

            ((TextView) rootView.findViewById(R.id.movie_length)).setText(Run_Time + " min");
        }

        else if((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT) && !networkStatus)
        {
            // Setting the Image
            ImageView imageView = (ImageView) rootView.findViewById(R.id.details_poster);
            imageView.setImageResource(R.drawable.movie1);

            ((TextView) rootView.findViewById(R.id.movie_title)).setText("Blah Blah"); // Movie Title
            ((TextView) rootView.findViewById(R.id.story_detail)).setText("Blah Blah"); // Movie Story
            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText("0.0" + " / 10");

            ((TextView) rootView.findViewById(R.id.release_date_value)).setText("N/A");
        }

        return(rootView);
    }

    private boolean getMovieData(String movie_choice, String video_choice)
    {

        DownloadTask downloadTask = new DownloadTask();

        try
        {
            downloadTask.execute(movie_choice,video_choice).get();

            final_values = getJSONData(Movies_Data);
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    private String[] getJSONData(String json_data)
    {

        StringBuilder stringBuilder = new StringBuilder();

        try
        {
            // Parsing the JSON Data in the form of a String
            JSONObject reader = new JSONObject(json_data);

            // Fetching the RunTime for the Movie or TV Episode
            if(Video_Choice.equals("movie"))
            {
                stringBuilder.append(reader.getString("runtime"));
                stringBuilder.append("=");
            }
            else if(Video_Choice.equals("tv"))
            {
                JSONArray jsonArray = reader.getJSONArray("episode_run_time");

                String run_time = jsonArray.getString(0);

                stringBuilder.append(run_time);
                stringBuilder.append("=");
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("MoviesApp JSONException", " JSONException Occurred in getJSONData Function");
        }

        return(stringBuilder.toString().split("="));
    }

    // This class is used to download the Data using the theMovieDB API using the BackGround Thread.
    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            return(getData(urls[0], urls[1]));
        }

        public String getData(String url, String Video_Type)
        {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";
            final String API_Key = BuildConfig.THE_MOVIE_DB_API_KEY;
            final String ID = "api_key";

            Uri BuiltUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(Video_Type).appendPath(url).appendQueryParameter(ID, API_Key).build();

            String BuiltURL = BuiltUri.toString();

            Log.d("Movie URL", BuiltURL);

            try
            {
                URL web_url = new URL(BuiltURL);

                // Creates a request to TheMovieDB Website and opens the Connection
                httpURLConnection = (HttpURLConnection) web_url.openConnection();
                httpURLConnection.setRequestMethod("GET"); // Letting the Connection know that the data will be fetched
                httpURLConnection.connect(); // Connecting to the Web Link

                // Read the Input Stream into the String
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                // This happens when there is no data being read from the Opened Connection to Web URL.
                if(inputStream == null)
                {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                // Now reading the raw JSON Data into BufferReader
                while((line=bufferedReader.readLine())!= null)
                {
                    buffer.append(line);
                }

                // This might happen when read from URL becomes unsuccessful or may be the connection might drop in between
                if(buffer.length() == 0)
                {
                    return null;
                }

                Movies_Data = buffer.toString();

                // Log.i("TheMovieDB JSON Data:", Movies_Data);
            }

            // This Exception will occur whenever there is MALFORMED URL
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                Log.e("MoviesApp MalformedURL:", " Malformed URL Exception Occurred");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e("MoviesApp IOException: ", "IO Exception Occurred");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("MoviesApp Exception: "," Unknown Exception Occurred");
            }

            // Here Closing all the Open Network Connections and the BufferedReaders.
            finally
            {
                // Checking to see if the connection to web URL is still Open. If yes, then close it.
                if(httpURLConnection != null)
                {
                    httpURLConnection.disconnect();
                }

                if(bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        Log.e("MoviesApp IOException: ", " IO Exception occurred while closing the reader");
                    }
                }
            }

            return(Movies_Data);
        }

        @Override
        protected void onPostExecute(String string)
        {
            super.onPostExecute(string);
/*

            int index = 0;

            // Creating the URL List for all the Poster Thumbnails in here
            try
            {

                for (String s : final_values)
                {
                    if (s != null)
                    {
                        // Changing to the latest Story Overview
                        movie_desc[index] = s;
                        index += 1;
                    }
                }
            }

            catch (NullPointerException e)
            {
                e.printStackTrace();
                Log.e("Accessing Null String", "Null String Array Detected");
            }

            catch(ArrayIndexOutOfBoundsException e)
            {
                e.printStackTrace();
                Log.e("Invalid Indices Access" ,"more than 10 images detected");
            }

            return;
*/
        }
    }


}