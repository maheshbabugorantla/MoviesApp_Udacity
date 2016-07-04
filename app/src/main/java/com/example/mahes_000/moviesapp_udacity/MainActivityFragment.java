package com.example.mahes_000.moviesapp_udacity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// JSON Parsing Libraries
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment
{
    String[] final_values = null;

    private GridView gridView;
    private ProgressBar progressBar;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<ImageItem> mGridData;

    String Movies_Data = null; // This will contain the raw JSON Data that needs to parsed.

    int scrollIndex = 0;

    Parcelable state = null;

    // These are all the Dummy Web links and movie descriptions.
    String[] urls = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://orig00.deviantart.net/12fd/f/2015/243/0/c/albumcoverfor_benprunty_fragments_sylviaritter_by_faith303-d97uftr.png","http://image.tmdb.org/t/p/w185//vDwphkloD7ToaDpKASAXGgHOclN.jpg","https://image.tmdb.org/t/p/w185//HcVs1vI9XRXIzj0SIbZAbhJnyo.jpg","https://image.tmdb.org/t/p/w185//m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg","https://image.tmdb.org/t/p/w185/2cNZTfT3jCcI4Slin3jpHKmA2Ge.jpg","http://image.tmdb.org/t/p/w185/2EhWnRunP8dt6F0KyeIQPDykZcV.jpg","https://image.tmdb.org/t/p/w185/tCOciAMFKth9iyoMkaibz4uroxi.jpg","https://s-media-cache-ak0.pinimg.com/236x/3b/49/b5/3b49b5881843e77fa0b2e6d1e3035687.jpg","https://s-media-cache-ak0.pinimg.com/236x/d7/64/12/d764122bd97790f871f3e6878aa1bbc8.jpg","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://orig00.deviantart.net/12fd/f/2015/243/0/c/albumcoverfor_benprunty_fragments_sylviaritter_by_faith303-d97uftr.png","http://image.tmdb.org/t/p/w185//vDwphkloD7ToaDpKASAXGgHOclN.jpg","https://image.tmdb.org/t/p/w185//HcVs1vI9XRXIzj0SIbZAbhJnyo.jpg","https://image.tmdb.org/t/p/w185//m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg","https://image.tmdb.org/t/p/w185/2cNZTfT3jCcI4Slin3jpHKmA2Ge.jpg","http://image.tmdb.org/t/p/w185/2EhWnRunP8dt6F0KyeIQPDykZcV.jpg","https://image.tmdb.org/t/p/w185/tCOciAMFKth9iyoMkaibz4uroxi.jpg","https://s-media-cache-ak0.pinimg.com/236x/3b/49/b5/3b49b5881843e77fa0b2e6d1e3035687.jpg","https://s-media-cache-ak0.pinimg.com/236x/d7/64/12/d764122bd97790f871f3e6878aa1bbc8.jpg"};

    ArrayList<String> movie_desc = new ArrayList<>();

    // This is used to select between the "popular" and "top_rated"
    String Movies_Choice = "top_rated";
    String Video_Choice = "movies";

    public MainActivityFragment()
    {
        setHasOptionsMenu(true);
    }

    private boolean getMovieData(String movie_choice, String video_choice, String page_no)
    {

        DownloadTask downloadTask = new DownloadTask();

        try
        {
            downloadTask.execute(movie_choice,video_choice,page_no).get();
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
        // This will be returned when the parsing doesn't work as expected.
        String[] Data_movies = null;

        try
        {
            // Parsing the JSON Data in the form of a String
            JSONObject reader = new JSONObject(json_data);

            JSONArray results_obj = reader.getJSONArray("results");

            // Initializing the String Array to store the data for the Individual Movies
            Data_movies = new String[results_obj.length()];

            for(int index = 0; index < results_obj.length(); index++)
            {
                ImageItem imageItem = new ImageItem();

                JSONObject jsonObject = results_obj.getJSONObject(index);

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(jsonObject.getString("poster_path"));
                stringBuilder.append("=");

                //stringBuilder.append(jsonObject.getString("adult")); // When you uncomment this please make sure that you change the indexes in Movie_DetailsActivityFragment.java
                //stringBuilder.append("=");

                stringBuilder.append(jsonObject.getString("overview"));
                stringBuilder.append("=");

                if(Video_Choice.equals("movie"))
                {
                    stringBuilder.append(jsonObject.getString("title"));
                    stringBuilder.append("=");
                }
                else if(Video_Choice.equals("tv"))
                {
                    stringBuilder.append(jsonObject.getString("name"));
                    stringBuilder.append("=");
                }

                stringBuilder.append(jsonObject.getString("vote_average"));
                stringBuilder.append("=");
                stringBuilder.append(jsonObject.getString("popularity"));
                stringBuilder.append("=");

                if(Video_Choice.equals("movie"))
                {
                    stringBuilder.append(jsonObject.getString("release_date"));
                    stringBuilder.append("=");
                    imageItem.setTitle(jsonObject.getString("release_date").split("-")[0]);
                }

                else if(Video_Choice.equals("tv"))
                {
                    stringBuilder.append(jsonObject.getString("first_air_date"));
                    stringBuilder.append("=");
                    imageItem.setTitle(jsonObject.getString("first_air_date").split("-")[0]);
                }

                stringBuilder.append(Video_Choice); // Giving the Video Choice
                stringBuilder.append("=");

                stringBuilder.append(jsonObject.getString("id"));

                String image_url = "http://image.tmdb.org/t/p/w342/" + jsonObject.getString("poster_path");
                imageItem.setImage(image_url);

                mGridData.add(imageItem);

                Data_movies[index] = stringBuilder.toString();
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("MoviesApp JSONException", " JSONException Occurred in getJSONData Function");
        }

        return(Data_movies);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mGridData = new ArrayList<>();
        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, mGridData);

        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent toDetailActivity = new Intent(getActivity(), Movie_DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, movie_desc.get(position));

                startActivity(toDetailActivity);
            }
        });

        gridView.setOnScrollListener( new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
/*
                scrollIndex = gridView.getFirstVisiblePosition();
*/

                if(page == 1)
                {
                    return false;
                }
                else
                {
                    return LoadMoreData(Integer.toString(page));
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        return(rootView);
    }

    // This function is called when the fragment is available for the user to Start Interacting
    @Override
    public void onResume()
    {
        if(state != null)
        {
            gridView.onRestoreInstanceState(state);
            Log.d("MainActivityFragment", "trying to restore gridView state..");
        }

        super.onResume();

/*
        if(scrollIndex != 0)
        {
            gridView.smoothScrollToPosition(scrollIndex);
        }
*/
    }

    @Override
    public void onPause()
    {
//        scrollIndex = gridView.getFirstVisiblePosition();
        state = gridView.onSaveInstanceState();
        Log.d("MainActivityFragment", "trying to save gridView state..");
        super.onPause();
    }



    public boolean LoadMoreData(String page)
    {
        return(getMovieData(Movies_Choice, Video_Choice, page));
    }

    // This pull all the data from theMovieDB using the default settings as soon as the app starts
    @Override
    public void onStart()
    {
        super.onStart();

        Log.d("Inside OnStart Function", "onStart Started");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Movies_Choice = sharedPreferences.getString(getString(R.string.pref_rating_choice_key),getString(R.string.pref_rating_choice_default));
        Video_Choice = sharedPreferences.getString(getString(R.string.pref_video_choice_key),getString(R.string.pref_video_choice_default));

        System.out.println("Movies Choice " + Movies_Choice);
        System.out.println("Movies Choice " + Video_Choice);

        //Log.i("MovieApp Movies Choice:", Movies_Choice);

        if(isNetworkAvailable())
        {
            mGridData.clear(); // Clear all the data related to a Specific Video Type ("TV" or "Movie")
            movie_desc.clear(); // resetting all Movie Details
            Log.d("MainActivityFragment ", "Inside OnStart() Function");
            getMovieData(Movies_Choice, Video_Choice, "1");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            mGridData.clear();
            movie_desc.clear();
            Toast.makeText(getActivity(),"Make Sure that you are connected to Internet and Re-open the App", Toast.LENGTH_LONG).show();
/*
            gridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, mGridData);
*/
        }
    }

    // This class is used to download the Data using the theMovieDB API using the BackGround Thread.
    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            return(getData(urls[0], urls[1], urls[2]));
        }

        public String getData(String url, String Video_Type, String page_no)
        {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";
            final String API_Key = BuildConfig.THE_MOVIE_DB_API_KEY;
            final String ID = "api_key";
            final String Page = "page";

            Uri BuiltUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(Video_Type).appendPath(url).appendQueryParameter(ID, API_Key).appendQueryParameter(Page,page_no).build();

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

                //Log.i("TheMovieDB JSON Data:", Movies_Data);
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

            // Creating the URL List for all the Poster Thumbnails in here
            try
            {
                gridViewAdapter.setGridData(mGridData);

                for (String s : final_values)
                {
                    if (s != null)
                    {
                        // Changing to the latest Story Overview
                        movie_desc.add(s);
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

        }
    }

}