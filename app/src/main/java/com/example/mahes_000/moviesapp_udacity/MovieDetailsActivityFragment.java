package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahes_000.moviesapp_udacity.moviedata.MovieContract;
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
public class MovieDetailsActivityFragment extends Fragment {

    private String Movies_Data = null;
    private String[] final_values = null;
    private String Video_Choice = null;

    private Context mContext;

    public MovieDetailsActivityFragment() {
        setHasOptionsMenu(true);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Creating a rootView
        View rootView = inflater.inflate(R.layout.fragment_movie__details, container, false);

        Intent intent = getActivity().getIntent();

        boolean networkStatus = isNetworkAvailable();

        mContext = getContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Video_Choice = sharedPreferences.getString(mContext.getString(R.string.pref_video_choice_key), mContext.getString(R.string.pref_video_choice_default));

        // Used as Projections to fetch the Specific Movie/TV Show Details
        String[] Movie_Columns = {MovieContract.MovieEntry.COLUMN_OVERVIEW, MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MovieContract.MovieEntry.COLUMN_TITLE, MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, MovieContract.MovieEntry.COLUMN_POSTER_PATH};

        String[] TV_Columns = {MovieContract.TVEntry.COLUMN_OVERVIEW, MovieContract.TVEntry.COLUMN_RELEASE_DATE, MovieContract.TVEntry.COLUMN_TITLE, MovieContract.TVEntry.COLUMN_VOTE_AVERAGE, MovieContract.TVEntry.COLUMN_POSTER_PATH};

        // Order of the Rows
        int COL_OVERVIEW_INDEX = 0;
        int COL_RELEASE_DATE_INDEX = 1;
        int COL_TITLE_INDEX = 2;
        int COL_VOTE_AVERAGE_INDEX = 3;
        int COL_POSTER_PATH_INDEX = 4;

        if ((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT) && networkStatus) {

            String text_detail = intent.getStringExtra(Intent.EXTRA_TEXT);

/*
            String[] str_values = text_detail.split("=");
*/

            // Fetching the Movie or TVShow Details
            Cursor movie_Details = null;

            if(Video_Choice.equals("movie")) {
                movie_Details = getContext().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(text_detail)), Movie_Columns, null, null, null);
            }
            else if(Video_Choice.equals("tv"))
            {
                movie_Details = getContext().getContentResolver().query(MovieContract.TVEntry.buildTVUri(Long.parseLong(text_detail)), TV_Columns, null, null, null);
            }

            if(movie_Details.moveToFirst()) {
                String Image_Base_URL = "http://image.tmdb.org/t/p/w185/";
                String Image_URL = Image_Base_URL + movie_Details.getString(COL_POSTER_PATH_INDEX);

                // Setting the Image for the Poster
                Picasso.with(getActivity()).load(Image_URL).fit().into((ImageView) rootView.findViewById(R.id.details_poster));

                ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie_Details.getString(COL_TITLE_INDEX)); // Movie Title
                ((TextView) rootView.findViewById(R.id.story_detail)).setText(movie_Details.getString(COL_OVERVIEW_INDEX)); // Movie Story
                ((TextView) rootView.findViewById(R.id.user_rating_value)).setText(movie_Details.getString(COL_VOTE_AVERAGE_INDEX) + "/10"); // User Rating Value

                ((TextView) rootView.findViewById(R.id.release_date_value)).setText(movie_Details.getString(COL_RELEASE_DATE_INDEX).split("-")[0]);  // Release Date (Movie) or First Aired Date (TV)
            }

/*
            Video_Choice = str_values[6];
*/
            String Movie_ID = text_detail;
/*
                    str_values[7];
*/

            // Fetching the Data for the Movie.
            getMovieData(Movie_ID, Video_Choice);

            String Run_Time = final_values[0];

            ((TextView) rootView.findViewById(R.id.movie_length)).setText(Run_Time + " min");
        } else if ((intent != null) && intent.hasExtra(Intent.EXTRA_TEXT) && !networkStatus) {
            // Setting the Image
            ImageView imageView = (ImageView) rootView.findViewById(R.id.details_poster);
            imageView.setImageResource(R.drawable.movie1);

            ((TextView) rootView.findViewById(R.id.movie_title)).setText("Blah Blah"); // Movie Title
            ((TextView) rootView.findViewById(R.id.story_detail)).setText("Blah Blah"); // Movie Story
            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText("0.0" + " / 10");

            ((TextView) rootView.findViewById(R.id.release_date_value)).setText("N/A");
            ((TextView) rootView.findViewById(R.id.movie_length)).setText("0 min");
        }

        return (rootView);
    }

    private boolean getMovieData(String movie_choice, String video_choice) {

        DownloadTask downloadTask = new DownloadTask();

        try {
            downloadTask.execute(movie_choice, video_choice).get();

            final_values = getJSONData(Movies_Data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return true;
    }

    private String[] getJSONData(String json_data) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Parsing the JSON Data in the form of a String
            JSONObject reader = new JSONObject(json_data);

            // Fetching the RunTime for the Movie or TV Episode
            if (Video_Choice.equals("movie")) {
                stringBuilder.append(reader.getString("runtime"));
                stringBuilder.append("=");
            } else if (Video_Choice.equals("tv")) {
                JSONArray jsonArray = reader.getJSONArray("episode_run_time");

                String run_time = jsonArray.getString(0);

                stringBuilder.append(run_time);
                stringBuilder.append("=");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MoviesApp JSONException", " JSONException Occurred in getJSONData Function");
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        return (stringBuilder.toString().split("="));
    }

    // This class is used to download the Data using the theMovieDB API using the BackGround Thread.
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

            Uri BuiltUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(Video_Type).appendPath(url).appendQueryParameter(ID, API_Key).build();

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