package com.example.mahes_000.moviesapp_udacity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

// Picasso Library Import
import com.squareup.picasso.Picasso;

// JSON Parsing Libraries
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment {

    String[] final_values = null;
    View MainView;

    // These are all the Dummy Web links and movie descriptions.
    String[] urls = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://orig00.deviantart.net/12fd/f/2015/243/0/c/albumcoverfor_benprunty_fragments_sylviaritter_by_faith303-d97uftr.png","http://image.tmdb.org/t/p/w185//vDwphkloD7ToaDpKASAXGgHOclN.jpg","https://image.tmdb.org/t/p/w185//HcVs1vI9XRXIzj0SIbZAbhJnyo.jpg","https://image.tmdb.org/t/p/w185//m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg","https://image.tmdb.org/t/p/w185/2cNZTfT3jCcI4Slin3jpHKmA2Ge.jpg","http://image.tmdb.org/t/p/w185/2EhWnRunP8dt6F0KyeIQPDykZcV.jpg","https://image.tmdb.org/t/p/w185/tCOciAMFKth9iyoMkaibz4uroxi.jpg","https://s-media-cache-ak0.pinimg.com/236x/3b/49/b5/3b49b5881843e77fa0b2e6d1e3035687.jpg","https://s-media-cache-ak0.pinimg.com/236x/d7/64/12/d764122bd97790f871f3e6878aa1bbc8.jpg","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://orig00.deviantart.net/12fd/f/2015/243/0/c/albumcoverfor_benprunty_fragments_sylviaritter_by_faith303-d97uftr.png","http://image.tmdb.org/t/p/w185//vDwphkloD7ToaDpKASAXGgHOclN.jpg","https://image.tmdb.org/t/p/w185//HcVs1vI9XRXIzj0SIbZAbhJnyo.jpg","https://image.tmdb.org/t/p/w185//m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg","https://image.tmdb.org/t/p/w185/2cNZTfT3jCcI4Slin3jpHKmA2Ge.jpg","http://image.tmdb.org/t/p/w185/2EhWnRunP8dt6F0KyeIQPDykZcV.jpg","https://image.tmdb.org/t/p/w185/tCOciAMFKth9iyoMkaibz4uroxi.jpg","https://s-media-cache-ak0.pinimg.com/236x/3b/49/b5/3b49b5881843e77fa0b2e6d1e3035687.jpg","https://s-media-cache-ak0.pinimg.com/236x/d7/64/12/d764122bd97790f871f3e6878aa1bbc8.jpg"};
    String[] movie_desc = {"Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah","Blah, Blah"};
    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();

    // This is used to select between the "popular" and "top_rated"
    String Movies_Choice = "top_rated";
    String Video_Choice = "movies";

    public MainActivityFragment()
    {
        setHasOptionsMenu(true);
    }

    private boolean getMovieData(String movie_choice, String video_choice)
    {

        DownloadTask downloadTask = new DownloadTask();

        try
        {
            downloadTask.execute(movie_choice,video_choice).get();
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

            int maxIndex;

            if(results_obj.length() < 20)
            {
                maxIndex = results_obj.length();
            }

            else
            {
                maxIndex = 20;
            }

            for(int index = 0; index < maxIndex; index++)
            {
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
                }

                else if(Video_Choice.equals("tv"))
                {
                    stringBuilder.append(jsonObject.getString("first_air_date"));
                    stringBuilder.append("=");
                }

                stringBuilder.append(Video_Choice); // Giving the Video Choice

                //Log.i("Movie: ", stringBuilder.toString());

                Data_movies[index] = stringBuilder.toString();
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("MoviesApp JSONException", " JSONException Occurred in getJSONData Function");
        }

        for(String s: Data_movies)
        {
            if (s != null) {
                //System.out.println("Inside JSON Function: " + s);
            }
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

        rootView = displayMovieData(rootView);

        return(rootView);
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

//        System.out.println("Movies Choice " + Movies_Choice);
//        System.out.println("Movies Choice " + Video_Choice);

        //Log.i("MovieApp Movies Choice:", Movies_Choice);

        if(isNetworkAvailable())
        {
            getMovieData(Movies_Choice, Video_Choice);
        }

        return;
    }

    public View displayMovieData(View rootView)
    {
        // The Code is below is to make the App work with a web API

        /* 1. First Check for Internet Connectivity if not throw a Toast Notification Requesting the User to check for their Internet Connection.
              REMEMBER: In case there is no internet connection, the app will show stored dummy data for the movies.

           2. i)   If Internet is available, Fetch the Images from the theMovieDB
              ii)  Do any Image Conversion as required,
              iii) Compress the Images based on the image size and set them to the respective IDs of ImageViews

           3. Create a list of URLs to fetch the images from

         */

        // Checking for the Internet Connectivity on the Phone.
        boolean NetworkStatus = isNetworkAvailable();

        // List of all ImageView IDs
        int[] array_ids = {R.id.Option_1, R.id.Option_2, R.id.Option_3, R.id.Option_4, R.id.Option_5, R.id.Option_6, R.id.Option_7, R.id.Option_8, R.id.Option_9, R.id.Option_10, R.id.Option_11, R.id.Option_12, R.id.Option_13, R.id.Option_14, R.id.Option_15, R.id.Option_16, R.id.Option_17, R.id.Option_18, R.id.Option_19, R.id.Option_20};

        // This is the Link to get all the Popular Movies from MovieDb.
        //    String popular_movies = "http://api.themoviedb.org/3/movie/popular?api_key=2117c386bdd865562ccf2a210dfc49ef";

        int count = 0;

        if(NetworkStatus == true)
        {
            // This below loop adds all the ImageViews to the ArrayList
            for (int val : array_ids)
            {
                imageViewArrayList.add((ImageView) rootView.findViewById(val));

                count += 1;
                count = count % urls.length;
            }

        }

        // Loading Dummy Images
        else
        {
            // Letting the User know that the App is not connected to internet.
            Toast.makeText(getActivity(), "Please make sure you are connected to a network", Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "Loading Dummy Images and Data", Toast.LENGTH_SHORT).show();

            // This below loop adds all the ImageViews to the ArrayList
            for (int val : array_ids)
            {
                imageViewArrayList.add((ImageView) rootView.findViewById(val));
            }
        }

        // Setting the OnClick Listener for the ImageViews
        for (final ImageView imageView : imageViewArrayList)
        {
            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    // Getting the ID Tag
                    String Movie_Details = imageView.getTag().toString();

                    String Story_overview = movie_desc[Integer.parseInt(Movie_Details)];

                    // Sending the Story Line
                    Intent toDetailActivity = new Intent(getActivity(), Movie_DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, Story_overview);
                    startActivity(toDetailActivity);
                }
            });
        }

        return(rootView);
    }

    // This class is used to download the Data using the theMovieDB API using the BackGround Thread.
    public class DownloadTask extends AsyncTask<String, Void,String[]>
    {

        @Override
        protected String[] doInBackground(String... urls)
        {
            String[] parsed_values = getData(urls[0], urls[1]);

            return parsed_values;
        }

        public String[] getData(String url, String Video_Type) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";
            final String CHOICE_VIDEO = Video_Type;
            final String API_Key = BuildConfig.THE_MOVIE_DB_API_KEY;
            final String ID = "api_key";

            // http://api.themoviedb.org/3/movie/popular?api_key=2117c386bdd865562ccf2a210dfc49ef

            Uri BuiltUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(CHOICE_VIDEO).appendPath(url).appendQueryParameter(ID, API_Key).build();

            String BuiltURL = BuiltUri.toString();

            Log.d("Movie URL", BuiltURL);

            try
            {
                URL web_url = new URL(BuiltURL);

                // Creates a request to TheMovieDB Website and opens the Connection
                httpURLConnection = (HttpURLConnection) web_url.openConnection();
                httpURLConnection.setRequestMethod("GET"); // Letting the Connection know that the data will be fetched
                httpURLConnection.connect(); // Connecting to the Web Link

                String Movies_Data = null; // This will contain the raw JSON Data that needs to parsed.

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

                final_values = getJSONData(Movies_Data);
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

            return(final_values);
        }

        @Override
        protected void onPostExecute(String[] strings)
        {
            super.onPostExecute(strings);

            String Image_Base_URL = "http://image.tmdb.org/t/p/w185/";

            int index = 0;

            // Creating the URL List for all the Poster Thumbnails in here
            try
            {
                for (String s : strings)
                {
                    if (s != null)
                    {
                        //System.out.println(s);
                        String[] new_data = s.split("=", 2);
                        urls[index] = Image_Base_URL + new_data[0];

                        // Updated imageView with new Film AlbumArts
                        Picasso.with(getActivity()).load(urls[index]).fit().into(imageViewArrayList.get(index));

                        // Changing to the latest Story Overview
                        movie_desc[index] = new_data[1];
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
        }
    }

}